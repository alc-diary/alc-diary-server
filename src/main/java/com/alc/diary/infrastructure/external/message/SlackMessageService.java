package com.alc.diary.infrastructure.external.message;

import com.alc.diary.application.message.MessageService;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.IIOException;

@Slf4j
@Component
public class SlackMessageService implements MessageService {

    private final MethodsClient methods;

    public SlackMessageService(@Value("${slack.token}") String slackToken) {
        log.info(slackToken);
        this.methods = Slack.getInstance().methods(slackToken);
    }

    @Override
    public void send(String channel, String message) {
        ChatPostMessageRequest slackMessage = ChatPostMessageRequest.builder()
                .channel(channel)
                .text(message)
                .build();
        try {
            methods.chatPostMessage(slackMessage);
        } catch (SlackApiException e) {
            log.error("error", e);
        } catch (IIOException e) {
            log.error("error", e);
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
