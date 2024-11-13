package com.example.demo.appevent;

import org.springframework.context.ApplicationEvent;

public class ESEvent<T, ID> extends ApplicationEvent {

    private final Class<T> entityClass;

    public ESEvent(Object source, Class<T> entityClass) {
        super(source);
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

}