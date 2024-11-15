package com.example.demo.Invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PersonInvocationHandler implements InvocationHandler {
    private final Object target;

    public PersonInvocationHandler(Object target) {
        this.target = target; // 目标对象
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 方法执行前的自定义操作
        System.out.println("Before invoking " + method.getName());

        // 调用目标对象的方法
        Object result = method.invoke(target, args);

        // 方法执行后的自定义操作
        System.out.println("After invoking " + method.getName());

        return result;
    }
}
