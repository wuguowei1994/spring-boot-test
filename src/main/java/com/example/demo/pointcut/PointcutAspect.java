package com.example.demo.pointcut;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PointcutAspect {

    // 定义一个切入点，匹配所有在 MyService 类中的 public 方法
    @Pointcut("execution(public void com.example.demo.pointcut.PointcutService.*(..))")
    public void myServiceMethods() {
        // 这个方法没有方法体，它只是一个标记方法，用来定义切入点
    }

    // 方法执行前的拦截
    @Before("myServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before method: " + joinPoint.getSignature().getName());
    }

    // 方法执行后的拦截
    @After("myServiceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After method: " + joinPoint.getSignature().getName());
    }

    // 环绕通知，用于记录方法的执行时间
    @Around("myServiceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 执行目标方法
        long end = System.currentTimeMillis();
        System.out.println("Method " + joinPoint.getSignature().getName() + " executed in " + (end - start) + "ms");
        return result;
    }
}
