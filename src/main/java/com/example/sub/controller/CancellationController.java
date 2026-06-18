package com.example.sub.controller;

import com.example.sub.repository.CancellationLogRepository;
import com.example.sub.repository.MemberRepository;
import com.example.sub.service.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/cancellation")
@RequiredArgsConstructor
public class CancellationController {

    private final CancellationService cancellationService;
    private final CancellationLogRepository logRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/request/{subscriptionId}")
    public String requestForm(@PathVariable Long subscriptionId, Model model) {
        model.addAttribute("subscriptionId", subscriptionId);
        return "subscribe/cancel-request";
    }

// 임시: Security 없을 때
@PostMapping("/cancel")
public String cancel(@RequestParam Long memberId,  // 임시로 직접 받음
                     @RequestParam Long subscriptionId,
                     @RequestParam String reason) {
    cancellationService.cancelSubscription(memberId, subscriptionId, reason);
    return "redirect:/my-subscriptions";
}

    @GetMapping("/recover")
    public String recover(@RequestParam String token, RedirectAttributes ra) {
        try {
            cancellationService.recoverByToken(token);
            ra.addFlashAttribute("message", "구독이 복구되었습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/my-subscriptions";
    }

    @GetMapping("/my-history")
    public String myHistory(Principal principal, Model model) {
        var member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));
        var logs = logRepository.findByMemberIdOrderByCancelledAtDesc(member.getId());
        model.addAttribute("logs", logs);
        return "member/cancellation-history";
    }
}