package com.example.demo.appevent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ESListener implements ApplicationListener<ESEvent> {

    @Override
    public void onApplicationEvent(ESEvent event) {
        // 处理事件时的逻辑
        System.out.println("Event received: " + event.getEntityClass().getName());
    }
}