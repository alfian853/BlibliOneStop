package com.gdn.onestop.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface MessagingService {
    Mono<Boolean> pushMessageToFirebase(String topic, Map<String, String> data);
}
