package com.alc.diary.presentation.controller;

import com.alc.diary.application.user.UserAppService;
import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameTokens;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UserAppService userAppService;

    @GetMapping("/main")
    public String main() {
        return "admin-main";
    }

    @GetMapping("/nickname")
    public String nickname(Model model) {
        GetRandomNicknameTokens randomNicknameTokens = userAppService.getRandomNicknameTokens();
        model.addAttribute("randomNicknameTokens", randomNicknameTokens);
        return "admin-nickname";
    }

    @PostMapping("/nickname")
    public String nickname(
            @RequestParam NicknameTokenOrdinal ordinal,
            @RequestParam String keyword
    ) {
        userAppService.createRandomNicknameToken(new CreateRandomNicknameTokenAppRequest(ordinal, keyword));
        System.out.println(ordinal);
        System.out.println(keyword);
        return "redirect:/admin/nickname";
    }
}
