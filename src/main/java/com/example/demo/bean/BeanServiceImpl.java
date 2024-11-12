package com.example.demo.bean;

public class BeanServiceImpl implements BeanService {
    public final String init;

    @Override
    public String greet(String name) {
        return "Hello, " + name + "!, I'm BeanServiceImpl, init is " + init;
    }

    public BeanServiceImpl(String init) {
        this.init = init;
    }
}
