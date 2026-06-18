package com.example.sub.service;

import com.example.sub.domain.entity.SubscriptionUsage;
import com.example.sub.repository.SubscriptionUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsageStatisticsService {

    private final SubscriptionUsageRepository usageRepository;

    public Map<String, Object> getUsageStats(Long subscriptionId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(29); // 최근 30일

        List<SubscriptionUsage> usages = usageRepository.findByMemberSubscriptionIdAndUsedDateBetween(subscriptionId, start, end);
        
        long count = usages.stream().filter(SubscriptionUsage::isUsed).count();
        int totalMinutes = usages.stream()
                .filter(u -> u.getDurationMinutes() != null)
                .mapToInt(SubscriptionUsage::getDurationMinutes)
                .sum();

        return Map.of(
                "count", count,
                "totalMinutes", totalMinutes,
                "history", usages // 히트맵용 전체 기록
        );
    }
}
