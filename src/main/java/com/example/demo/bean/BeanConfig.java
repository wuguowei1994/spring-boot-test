package com.example.demo.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public BeanService beanService() {
        return new BeanServiceImpl("bean service");
    }

    @Bean
    public BeanController beanController(BeanService beanService) {
        return new BeanController(beanService);
    }
}
