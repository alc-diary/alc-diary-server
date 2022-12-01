package com.example.alcdiary.presentation.controller;

import com.example.alcdiary.application.DeleteUsernameKeywordUseCase;
import com.example.alcdiary.application.GetRandomKeywordListUseCase;
import com.example.alcdiary.application.SaveUsernameKeywordUseCase;
import com.example.alcdiary.application.command.DeleteUsernameKeywordCommand;
import com.example.alcdiary.application.command.SaveUsernameKeywordCommand;
import com.example.alcdiary.application.result.GetRandomKeywordListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {


    private final GetRandomKeywordListUseCase getRandomKeywordListUseCase;
    private final SaveUsernameKeywordUseCase saveUsernameKeywordUseCase;
    private final DeleteUsernameKeywordUseCase deleteUsernameKeywordUseCase;

    @GetMapping("/user")
    public String userTab(Model model) {
        GetRandomKeywordListResult result = getRandomKeywordListUseCase.execute();
        model.addAttribute("firstKeywordList", result.getFirstKeywordList());
        model.addAttribute("secondKeywordList", result.getSecondKeywordList());
        return "pages/user";
    }

    @PostMapping("/user/username/keyword")
    public String saveUsernameKeyword(
            @RequestParam String keyword,
            @RequestParam String location
    ) {
        SaveUsernameKeywordCommand command = new SaveUsernameKeywordCommand(keyword, location);
        saveUsernameKeywordUseCase.execute(command);
        return "redirect:/admin/user";
    }

    @PostMapping("/user/username/keyword/delete")
    public String deleteUsernameKeyword(
            @RequestParam String keyword,
            @RequestParam String location
    ) {
        DeleteUsernameKeywordCommand command = new DeleteUsernameKeywordCommand(keyword, location);
        deleteUsernameKeywordUseCase.execute(command);
        return "redirect:/admin/user";
    }

    @GetMapping("/login/kakao")
    public String kakaoLoginPage() {
        return "pages/kakao-login";
    }
}
