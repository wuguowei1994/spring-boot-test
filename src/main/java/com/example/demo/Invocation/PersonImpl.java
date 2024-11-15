package com.example.demo.Invocation;

public class PersonImpl implements Person {
    @Override
    public void sayHello(String name) {
        System.out.println("Hello, " + name);
    }
}
