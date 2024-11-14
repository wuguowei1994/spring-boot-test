package com.example.demo.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.support.GenericConversionService;

public class SpringConversionServiceExample {

    public static void main(String[] args) {
        // 使用 Spring IoC 容器
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 从容器中获取 ConversionService Bean
        GenericConversionService conversionService = context.getBean(GenericConversionService.class);

        String str = "456";
        Integer result = conversionService.convert(str, Integer.class);
        System.out.println("Converted to Integer: " + result);
    }
}
