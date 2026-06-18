package com.example.sub.service;

import com.example.sub.domain.entity.MemberSubscription;
import com.example.sub.domain.entity.SubscriptionUsage;
import com.example.sub.repository.MemberSubscriptionRepository;
import com.example.sub.repository.SubscriptionUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckInService {

    private final SubscriptionUsageRepository usageRepository;
    private final MemberSubscriptionRepository memberSubscriptionRepository;
    private final AlertLevelResolver alertLevelResolver;

    public void checkIn(Long subscriptionId, Integer durationMinutes) {
        MemberSubscription subscription = memberSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription ID"));

        LocalDate today = LocalDate.now();
        if (usageRepository.existsByMemberSubscriptionIdAndUsedDate(subscriptionId, today)) {
            throw new IllegalStateException("오늘은 이미 체크인했습니다.");
        }

        SubscriptionUsage usage = new SubscriptionUsage();
        usage.setMemberSubscription(subscription);
        usage.setUsedDate(today);
        usage.setUsed(true);
        usage.setDurationMinutes(durationMinutes);
        usageRepository.save(usage);

        // 마지막 사용일 및 경보 등급 업데이트
        subscription.setLastUsedAt(today);
        subscription.setAlertLevel(alertLevelResolver.resolve(subscription.getLastUsedAt()));
    }
}
