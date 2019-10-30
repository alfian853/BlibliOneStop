package com.gdn.onestop.service;

import java.util.Map;

public interface MessagingService {
    void pushMessageToFirebase(String topic, Map<String, String> data);
}
