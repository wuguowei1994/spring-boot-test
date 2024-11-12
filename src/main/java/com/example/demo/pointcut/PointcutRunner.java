package com.example.demo.pointcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PointcutRunner implements CommandLineRunner {

    @Autowired
    private PointcutService pointcutService;

    @Override
    public void run(String...args) throws Exception {
        //myService.doSomething();
        //myService.doAnotherThing();
    }
}