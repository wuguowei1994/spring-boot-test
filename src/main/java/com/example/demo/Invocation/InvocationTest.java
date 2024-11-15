package com.example.demo.Invocation;

import java.lang.reflect.Proxy;

public class InvocationTest {
    public static void main(String[] args) {
        // 创建目标对象
        Person person = new PersonImpl();

        // 创建 InvocationHandler
        PersonInvocationHandler handler = new PersonInvocationHandler(person);

        // 创建代理对象
        Person proxy = (Person) Proxy.newProxyInstance(
                Person.class.getClassLoader(),
                new Class<?>[] { Person.class },
                handler
        );

        // 调用代理对象的方法
        proxy.sayHello("John");
    }
}
