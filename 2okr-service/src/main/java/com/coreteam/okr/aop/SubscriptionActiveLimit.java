package com.coreteam.okr.aop;

import java.lang.annotation.*;

/**
 * @ClassName: SubscriptionActiveLimit
 * @Description 用来标注需要组织实现订阅才能调用的方法
 * @Author sean.deng
 * @Date 2019/09/09 15:02
 * @Version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscriptionActiveLimit {

    String organizationId() default "";

}
