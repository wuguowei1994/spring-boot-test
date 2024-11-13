package com.example.demo.appevent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ESRepoListener {

    // 处理 ESEvent 事件
    @EventListener
    public void handleBaseESRepositoryPreparedEvent(ESEvent<?, ?> event) {
        try {
            // 获取事件中携带的实体类类型信息
            Class<?> entityClass = event.getEntityClass();

            // 打印基本信息
            System.out.println("Received event for entity class: {}" + entityClass.getName());

        } catch (Exception e) {
            System.err.println("Error occurred while handling event: " + e);
        }
    }
}
