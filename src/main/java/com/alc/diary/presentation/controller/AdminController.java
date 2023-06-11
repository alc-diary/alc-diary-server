package com.alc.diary.presentation.controller;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameTokens;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final NicknameAppService nicknameAppService;

    @GetMapping("/main")
    public String main() {
        return "admin-main";
    }

    @GetMapping("/nickname")
    public String getNicknameTokens(Model model) {
        GetRandomNicknameTokens randomNicknameTokens = nicknameAppService.getRandomNicknameTokens();
        model.addAttribute("randomNicknameTokens", randomNicknameTokens);
        return "admin-nickname";
    }

    @PostMapping("/nickname")
    public String createNicknameToken(
            @RequestParam NicknameTokenOrdinal ordinal,
            @RequestParam String keyword
    ) {
        nicknameAppService.createRandomNicknameToken(new CreateRandomNicknameTokenAppRequest(ordinal, keyword));
        return "redirect:/admin/nickname";
    }

    @PostMapping("/nickname/delete")
    public String deleteNickname(
            @RequestParam Long tokenId
    ) {
        nicknameAppService.deleteNicknameToken(tokenId);
        return "redirect:/admin/nickname";
    }
}
