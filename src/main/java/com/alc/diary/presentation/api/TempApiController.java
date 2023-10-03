package com.alc.diary.presentation.api;

import com.alc.diary.application.message.MessageService;
import com.alc.diary.application.temp.TempService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/temp")
@RestController
public class TempApiController {

    private final TempService tempService;
    private final MessageService messageService;
    @Value("${slack.token}") String slackToken;

    @GetMapping("/migration")
    public void migration(@RequestParam long id) {
        tempService.migration(id);
    }

    @PostMapping("/slack-message")
    public String slackMessage() {
        log.info("테스트");
        log.info(slackToken);
        messageService.send("#알림", "테스트");
        return slackToken;
    }
}
