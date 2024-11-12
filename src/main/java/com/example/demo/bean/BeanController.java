package com.example.demo.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeanController {

    private final BeanService beanService;

    @Autowired
    public BeanController(BeanService beanService) {
        this.beanService = beanService;
    }

    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        return beanService.greet(name);  // 使用 MyService 的方法
    }
}
