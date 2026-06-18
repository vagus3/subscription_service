package com.example.sub.service;

import com.example.sub.domain.entity.CancellationLog;
import com.example.sub.domain.entity.MemberSubscription;
import com.example.sub.repository.CancellationLogRepository;
import com.example.sub.repository.MemberSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancellationService {

    private final CancellationLogRepository logRepository;
    private final MemberSubscriptionRepository memberSubscriptionRepository;

    @Transactional
    public void cancelSubscription(Long memberId, Long subscriptionId, String reason) {
        MemberSubscription subscription = memberSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("구독 없음"));
        if (!subscription.getMember().getId().equals(memberId)) 
            throw new SecurityException("본인 구독만 해지 가능");

        subscription.setStatus("CANCELLED");

        CancellationLog log = CancellationLog.builder()
                .member(subscription.getMember())
                .plan(subscription.getPlan())
                .cancelledAt(java.time.LocalDate.now())
                .memo(reason)
                .build();
        logRepository.save(log);
    }

    @Transactional
    public void recoverByToken(String token) {
        // 현재 CancellationLog 구조에서는 recovery token을 지원하지 않음
        throw new UnsupportedOperationException("Recovery 기능은 추후 업데이트 예정입니다.");
    }
}