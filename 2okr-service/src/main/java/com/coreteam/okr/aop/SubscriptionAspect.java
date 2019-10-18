package com.coreteam.okr.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @ClassName: SubscriptionAspect
 * @Description 组织订阅判断的拦截AOP
 * @Author sean.deng
 * @Date 2019/09/09 15:00
 * @Version 1.0.0
 */
@Component
@Aspect
public class SubscriptionAspect {


    @Autowired
    private OrganizationSubscriptionManager subscriptionManager;

    @Around("@annotation(com.coreteam.okr.aop.SubscriptionActiveLimit)")
    public Object checkSubscriptionActived(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        SubscriptionActiveLimit subscriptionActiveLimit =  (SubscriptionActiveLimit) method.getAnnotation(SubscriptionActiveLimit.class);
        Object organizationId = AnnotationResolver.resolver(joinPoint, subscriptionActiveLimit.organizationId());
        subscriptionManager.checkActived(Long.valueOf(organizationId.toString()));
        return joinPoint.proceed();

    }
}
