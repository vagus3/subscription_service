package com.example.sub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderService {

    // 매일 오전 9시 발송
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyReminders() {
        // 실제로는 구독 정보를 조회하여 만료 임박이나 방치 경보 등을 발송해야 함
        // 여기서는 예시로 로깅 또는 간단한 호출만 작성 (구독 정보 조회가 backend2 영역이라 연동 필요)
        System.out.println("Sending daily reminders at 9 AM...");
        // mailUtil.sendEmail("user@example.com", "Subscription Reminder", "Check your subscriptions!");
    }
}
