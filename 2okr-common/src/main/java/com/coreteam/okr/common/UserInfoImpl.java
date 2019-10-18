package com.coreteam.okr.common;

import org.springframework.stereotype.Component;

/**
 * @ClassName: UserInfoImpl
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/26 15:37
 * @Version 1.0.0
 */
@Component
public class UserInfoImpl implements UserInfo {
    @Override
    public long getUserId() {
        return 1L;
    }

    @Override
    public String getName() {
        return "unkown";
    }
}
