package com.example.sub.service;

import com.example.sub.domain.entity.AlertLevel;
import com.example.sub.domain.entity.Member;
import com.example.sub.domain.entity.MemberSubscription;
import com.example.sub.domain.entity.SubscriptionPlan;
import com.example.sub.repository.MemberRepository;
import com.example.sub.repository.MemberSubscriptionRepository;
import com.example.sub.repository.SubscriptionPlanRepository;
import com.example.sub.repository.SubscriptionUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final SubscriptionPlanRepository planRepository;
    private final MemberSubscriptionRepository memberSubscriptionRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionUsageRepository usageRepository;

    public List<SubscriptionPlan> findAllPlans() {
        return planRepository.findAll();
    }

    public SubscriptionPlan findPlanById(Long id) {
        return planRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid plan ID"));
    }

    public List<MemberSubscription> findMemberSubscriptions(Long memberId) {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        List<MemberSubscription> subscriptions = memberSubscriptionRepository.findByMemberId(memberId);
        subscriptions.forEach(subscription -> {
            if (subscription.getDueDate() != null) {
                subscription.setDaysUntilDue((int) ChronoUnit.DAYS.between(today, subscription.getDueDate()));
            }
            subscription.setMonthlyCheckInCount((int) usageRepository
                    .countByMemberSubscriptionIdAndUsedDateBetweenAndUsedTrue(subscription.getId(), monthStart, today));
        });
        return subscriptions;
    }

    @Transactional
    public void subscribe(Long memberId, Long planId, LocalDate startDate) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID"));

        MemberSubscription subscription = MemberSubscription.builder()
                .member(member)
                .plan(plan)
                .startDate(startDate)
                .dueDate(startDate.plusMonths(1))
                .status("ACTIVE")
                .lastUsedAt(startDate)
                .alertLevel(AlertLevel.NORMAL)
                .build();

        memberSubscriptionRepository.save(subscription);
    }

    @Transactional
    public void cancelSubscription(Long subscriptionId) {
        MemberSubscription subscription = memberSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription ID"));
        subscription.setStatus("CANCELLED");
    }

    @Transactional
    public void subscribe(MemberSubscription subscription) {
        memberSubscriptionRepository.save(subscription);
    }
}
