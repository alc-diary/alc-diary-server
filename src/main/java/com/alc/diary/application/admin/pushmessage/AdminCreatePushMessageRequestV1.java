package com.alc.diary.application.admin.pushmessage;

public record AdminCreatePushMessageRequestV1(

        String title,
        String body,
        String eventName
) {
}
