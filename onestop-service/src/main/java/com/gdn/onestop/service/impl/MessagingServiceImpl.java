package com.gdn.onestop.service.impl;

import com.gdn.onestop.service.MessagingService;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class MessagingServiceImpl implements MessagingService {

    @Override
    public void pushMessageToFirebase(String topic, Map<String, String> data) {

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH).build();

        Message message = Message.builder().setAndroidConfig(androidConfig).setTopic(topic)
                .putAllData(data)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
