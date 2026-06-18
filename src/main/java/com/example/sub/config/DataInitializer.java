package com.example.sub.config;

import com.example.sub.domain.entity.*;
import com.example.sub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final SubscriptionPlanRepository planRepository;
    private final MemberSubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (memberRepository.count() > 0) return;

        // Plans
        SubscriptionPlan netflix = planRepository.save(SubscriptionPlan.builder()
                .name("Netflix").category("Premium 4K").monthlyPrice(19990).yearlyPrice(239880).build());
        SubscriptionPlan spotify = planRepository.save(SubscriptionPlan.builder()
                .name("Spotify").category("Family Plan").monthlyPrice(15990).yearlyPrice(191880).build());
        SubscriptionPlan aws = planRepository.save(SubscriptionPlan.builder()
                .name("AWS Cloud").category("Developer Instance").monthlyPrice(245300).yearlyPrice(2943600).build());
        SubscriptionPlan adobe = planRepository.save(SubscriptionPlan.builder()
                .name("Adobe CC").category("Photography").monthlyPrice(52990).yearlyPrice(635880).build());
        planRepository.save(SubscriptionPlan.builder()
                .name("Slack").category("Pro").monthlyPrice(12500).yearlyPrice(150000).build());
        planRepository.save(SubscriptionPlan.builder()
                .name("Grammarly").category("Business").monthlyPrice(120000).yearlyPrice(1440000).build());

        // Demo user  (email: user@demo.com / password: demo1234)
        Member member = memberRepository.save(Member.builder()
                .email("user@demo.com")
                .password(passwordEncoder.encode("demo1234"))
                .name("Alex Rivera")
                .phone("010-0000-0000")
                .role(Role.USER)
                .build());

        // Admin user (email: admin@demo.com / password: admin1234)
        memberRepository.save(Member.builder()
                .email("admin@demo.com")
                .password(passwordEncoder.encode("admin1234"))
                .name("Admin")
                .role(Role.ADMIN)
                .build());

        LocalDate today = LocalDate.now();

        // Member subscriptions
        subscriptionRepository.saveAll(List.of(
                MemberSubscription.builder()
                        .member(member).plan(netflix)
                        .startDate(LocalDate.of(2023, 1, 12))
                        .dueDate(today.plusDays(3))
                        .status("ACTIVE").alertLevel(AlertLevel.NORMAL)
                        .lastUsedAt(today.minusDays(1)).build(),
                MemberSubscription.builder()
                        .member(member).plan(spotify)
                        .startDate(LocalDate.of(2022, 3, 5))
                        .dueDate(today.plusDays(15))
                        .status("ACTIVE").alertLevel(AlertLevel.WARNING)
                        .lastUsedAt(today).build(),
                MemberSubscription.builder()
                        .member(member).plan(aws)
                        .startDate(LocalDate.of(2021, 10, 28))
                        .dueDate(today.plusDays(12))
                        .status("ACTIVE").alertLevel(AlertLevel.NORMAL)
                        .lastUsedAt(today.minusDays(2)).build(),
                MemberSubscription.builder()
                        .member(member).plan(adobe)
                        .startDate(LocalDate.of(2023, 6, 10))
                        .dueDate(null)
                        .status("CANCELLED").alertLevel(AlertLevel.NORMAL)
                        .lastUsedAt(today.minusDays(90)).build()
        ));
    }
}
