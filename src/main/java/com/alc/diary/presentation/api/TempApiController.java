package com.alc.diary.presentation.api;

import com.alc.diary.application.message.MessageService;
import com.alc.diary.application.temp.TempService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/temp")
@RestController
public class TempApiController {

    private final TempService tempService;
    private final MessageService messageService;

    @GetMapping("/migration")
    public void migration(@RequestParam long id) {
        tempService.migration(id);
    }

    @PostMapping("/slack-message")
    public void slackMessage() {
        messageService.send("#알림", "테스트");
    }
}
