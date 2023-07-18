package com.alc.diary.presentation.controller;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameTokens;
import com.alc.diary.domain.nickname.BannedWord;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final NicknameAppService nicknameAppService;

    @GetMapping("/main")
    public String main() {
        return "admin-main";
    }

    @GetMapping("/nicknames")
    public String getNicknameTokens(Model model) {
        GetRandomNicknameTokens randomNicknameTokens = nicknameAppService.getRandomNicknameTokens();
        model.addAttribute("randomNicknameTokens", randomNicknameTokens);
        return "admin-nickname";
    }

    @GetMapping("/nicknames/black-list")
    public String getNicknameBlacklist(Model model) {
        List<BannedWord> bannedWords = nicknameAppService.getAllBannedWords();
        model.addAttribute("bannedWords", bannedWords);
        return "admin-nickname-black-list";
    }

    @PostMapping("/nicknames")
    public String createNicknameToken(
            @RequestParam NicknameTokenOrdinal ordinal,
            @RequestParam String keyword
    ) {
        nicknameAppService.createRandomNicknameToken(new CreateRandomNicknameTokenAppRequest(ordinal, keyword));
        return "redirect:/admin/nicknames";
    }

    @PostMapping("/nicknames/delete")
    public String deleteNickname(
            @RequestParam Long tokenId
    ) {
        nicknameAppService.deleteNicknameToken(tokenId);
        return "redirect:/admin/nicknames";
    }
}
