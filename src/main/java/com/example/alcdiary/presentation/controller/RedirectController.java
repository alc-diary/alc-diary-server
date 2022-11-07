package com.example.alcdiary.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/redirect")
@Controller
public class RedirectController {

    @GetMapping
    public String redirect(
            @RequestParam String code,
            Model model
    ) {
        model.addAttribute("code", code);
        return "redirect";
    }
}
