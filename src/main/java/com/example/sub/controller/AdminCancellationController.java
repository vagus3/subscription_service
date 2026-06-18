package com.example.sub.controller;

import com.example.sub.repository.CancellationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cancellation")
@RequiredArgsConstructor
public class AdminCancellationController {

    private final CancellationLogRepository logRepository;

    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        var logs = logRepository.findAll();
        // TODO: 향후 recovery 기능이 추가되면 필터링 로직 구현
        model.addAttribute("logs", logs);
        return "admin/cancellation-list";
    }
}
