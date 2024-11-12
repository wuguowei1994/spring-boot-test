package com.example.demo.pointcut;

import org.springframework.stereotype.Service;

@Service
public class PointcutService {
    public void doSomething() {
        System.out.println("Doing something...");
    }

    public void doAnotherThing() {
        System.out.println("Doing another thing...");
    }
}
