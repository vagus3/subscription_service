package com.example.sub.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {

    @GetMapping("/billing")
    public String billing(Model model) {
        model.addAttribute("yearlySpend", 4280.50);
        model.addAttribute("invoices", List.of(
                new Invoice("AWS Hosting Services", "#INV-2024-0012", "Jun 15, 2024", 142.60, "Visa •••• 4242"),
                new Invoice("Slack Technologies", "#INV-2024-0011", "Jun 12, 2024", 12.50, "PayPal"),
                new Invoice("Netflix Premium", "#INV-2024-0010", "Jun 08, 2024", 19.99, "Visa •••• 4242"),
                new Invoice("Grammarly Inc.", "#INV-2024-0009", "Jun 01, 2024", 120.00, "Amex •••• 1004")
        ));
        return "billing";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    @Getter
    @AllArgsConstructor
    public static class Invoice {
        private final String service;
        private final String invoiceId;
        private final String date;
        private final double amount;
        private final String method;
    }
}
