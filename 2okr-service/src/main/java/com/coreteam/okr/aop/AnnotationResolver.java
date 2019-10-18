package com.coreteam.okr.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @ClassName: AnnotationResolver
 * @Description 注解参数解析器，协助从方法参数中获取organization Id

 * @Author sean.deng
 * @Date 2019/09/09 15:11
 * @Version 1.0.0
 */
public class AnnotationResolver {
    /**
     * 解析注解上的值
     * @param joinPoint
     * @param str 需要解析的字符串
     * @return
     */
    public static Object resolver(JoinPoint joinPoint, String str) {

        if (str == null) {
            return null ;
        }

        Object value = null;
        if (str.matches("#\\{\\D*\\}")) {// 如果name匹配上了#{},则把内容当作变量
            String newStr = str.replaceAll("#\\{", "").replaceAll("\\}", "");
            if (newStr.contains(".")) { // 复杂类型
                try {
                    value = complexResolver(joinPoint, newStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                value = simpleResolver(joinPoint, newStr);
            }
        } else { //非变量
            value = str;
        }
        return value;
    }


    private static Object complexResolver(JoinPoint joinPoint, String str) throws Exception {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String[] names = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        String[] strs = str.split("\\.");

        for (int i = 0; i < names.length; i++) {
            if (strs[0].equals(names[i])) {
                Object obj = args[i];
                Method dmethod = obj.getClass().getDeclaredMethod(getMethodName(strs[1]), null);
                Object value = dmethod.invoke(args[i]);
                return getValue(value, 1, strs);
            }
        }

        return null;

    }

    private static Object getValue(Object obj, int index, String[] strs) {

        try {
            if (obj != null && index < strs.length - 1) {
                Method method = obj.getClass().getDeclaredMethod(getMethodName(strs[index + 1]), null);
                obj = method.invoke(obj);
                getValue(obj, index + 1, strs);
            }

            return obj;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static  String getMethodName(String name) {
        return "get" + name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
    }


    private static Object simpleResolver(JoinPoint joinPoint, String str) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] names = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < names.length; i++) {
            if (str.equals(names[i])) {
                return args[i];
            }
        }
        return null;
    }
}
