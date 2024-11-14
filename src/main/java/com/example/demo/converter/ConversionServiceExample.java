package com.example.demo.converter;

import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConversionServiceExample {

    public static void main(String[] args) {
        // 创建一个 GenericConversionService 实例
        GenericConversionService conversionService = new GenericConversionService();

        // 注册自定义的 String 到 Integer 的转换器
        conversionService.addConverter(new Converter<String, Integer>() {
            @Override
            public Integer convert(String source) {
                return Integer.valueOf(source);  // 将字符串转换为整数
            }
        });

        // 注册自定义的 String 到 Double 的转换器
        conversionService.addConverter(new Converter<String, Double>() {
            @Override
            public Double convert(String source) {
                return Double.valueOf(source);  // 将字符串转换为浮动数
            }
        });

        // string 到 日期的转换器
        conversionService.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(source);
                } catch (Exception e) {
                    return null;
                }
            }
        });

        // 执行类型转换
        String strToConvert = "123";
        Integer integerResult = conversionService.convert(strToConvert, Integer.class);
        Double doubleResult = conversionService.convert(strToConvert, Double.class);

        // 打印转换结果
        System.out.println("Converted to Integer: " + integerResult);
        System.out.println("Converted to Double: " + doubleResult);
        Date dateResult = conversionService.convert("2024-11-14", Date.class);
        System.out.println("Converted to Date: " + dateResult);
    }
}

