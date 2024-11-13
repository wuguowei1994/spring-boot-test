package com.example.demo.appevent;

import org.springframework.stereotype.Service;

@Service
public class ESService {

    private final ESPublisher eventPublisher;

    public ESService(ESPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void prepareRepository() {
        // 模拟准备仓库操作
        System.out.println("Preparing repository...");

        // 发布事件
        ESEvent<?, ?> event = new ESEvent<>(this, String.class);
        System.out.println("Publishing event: " + event);
        eventPublisher.publishEvent(event);
    }
}
