package com.coreteam.okr.manager;

import com.coreteam.okr.constant.NotifyBusinessType;
import com.coreteam.okr.constant.NotifyTypeEnum;

/**
 * @ClassName: Notify
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 9:48
 * @Version 1.0.0
 */
public interface Notify<T> {
    NotifyTypeEnum type();

    T message();

    NotifyBusinessType businessType();
}
