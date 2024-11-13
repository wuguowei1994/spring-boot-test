package com.example.demo.appevent;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ESPublisher implements ApplicationEventPublisher {

    private final ApplicationListener<ESEvent> listener;

    public ESPublisher(ApplicationListener<ESEvent> listener) {
        Assert.notNull(listener, "ApplicationListener must not be null!");
        this.listener = listener;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        if (event instanceof ESEvent) {
            System.out.println("BaseESRepositoryPreparedEvent publishing, event is " + event);
            // 将事件传递给事件监听器进行处理
            listener.onApplicationEvent((ESEvent) event);
        }
    }

    @Override
    public void publishEvent(Object event) {
        // This method is not used but must be implemented due to the interface
        System.out.println("Unsupported event type: {}" +  event);
    }
}