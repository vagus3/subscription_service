package com.example.sub.controller;

import com.example.sub.service.CheckInService;
import com.example.sub.service.AuthenticatedMemberService;
import com.example.sub.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final CheckInService checkInService;
    private final UsageStatisticsService usageStatisticsService;
    private final AuthenticatedMemberService authenticatedMemberService;

    @GetMapping("/subscriptions")
    public String mySubscriptions(Authentication authentication, Model model) {
        Long memberId = authenticatedMemberService.requireMember(authentication).getId();

        // Model 규약: subscriptions
        var subscriptions = subscriptionService.findMemberSubscriptions(memberId);
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("monthlySpend", subscriptions.stream()
                .filter(s -> !"CANCELLED".equals(s.getStatus()))
                .mapToInt(s -> s.getPlan().getMonthlyPrice())
                .sum());
        model.addAttribute("activeCount", subscriptions.stream().filter(s -> "ACTIVE".equals(s.getStatus())).count());
        model.addAttribute("expiringCount", subscriptions.stream().filter(s -> s.getDaysUntilDue() <= 7 && s.getDaysUntilDue() >= 0).count());
        model.addAttribute("savings", subscriptions.stream()
                .filter(s -> s.getAlertLevel() != null && s.getAlertLevel().name().matches("DANGER|CRITICAL"))
                .mapToInt(s -> s.getPlan().getMonthlyPrice())
                .sum());
        return "member/subscriptions";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam Long planId, 
                            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().toString()}") String startDate, 
                            Authentication authentication,
                            RedirectAttributes rttr) {
        Long memberId = authenticatedMemberService.requireMember(authentication).getId();
        subscriptionService.subscribe(memberId, planId, LocalDate.parse(startDate));
        
        rttr.addFlashAttribute("successMessage", "구독 신청이 완료되었습니다.");
        return "redirect:/subscriptions";
    }

    @PostMapping("/checkin/{id}")
    public String checkIn(@PathVariable Long id, 
                          @RequestParam(required = false) Integer durationMinutes,
                          RedirectAttributes rttr) {
        try {
            checkInService.checkIn(id, durationMinutes);
            rttr.addFlashAttribute("successMessage", "체크인이 완료되었습니다.");
        } catch (IllegalStateException e) {
            rttr.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/subscriptions/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes rttr) {
        subscriptionService.cancelSubscription(id);
        rttr.addFlashAttribute("successMessage", "구독이 해지되었습니다.");
        return "redirect:/subscriptions";
    }
}
