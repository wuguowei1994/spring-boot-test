package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private MyService myService;

    @Override
    public void run(String...args) throws Exception {
        myService.doSomething();
        myService.doAnotherThing();
    }
}