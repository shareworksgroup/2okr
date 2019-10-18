package com.coreteam.okr.common.util;

/**
 * @ClassName: ExceptionUtil
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 12:22
 * @Version 1.0.0
 */
public class ExceptionUtil {

    public static String stackTraceToString(Throwable throwable){
        StringBuffer buffer=new StringBuffer();
        StackTraceElement[] stackTraces = throwable.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraces) {
            buffer.append(stackTraceElement.toString()+"\n");
        }
        return buffer.toString();
    }
}
