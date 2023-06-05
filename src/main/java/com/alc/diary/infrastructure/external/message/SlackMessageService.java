package com.alc.diary.infrastructure.external.message;

import com.alc.diary.application.message.MessageService;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.IIOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class SlackMessageService implements MessageService {

    private static final MethodsClient methods = Slack.getInstance().methods("xoxb-3987321618736-5242072199362-jqAGxXmUVLB4gSlV7hGEoN5x");

    @Override
    public void send(String message) {
        ChatPostMessageRequest slackMessage = ChatPostMessageRequest.builder()
                                                             .channel("#알림")
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
