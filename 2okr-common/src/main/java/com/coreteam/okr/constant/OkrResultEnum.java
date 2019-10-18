package com.coreteam.okr.constant;

import com.coreteam.core.result.IResultEnumResult;

/**
 * okr 项目业务逻辑错误枚举类
 * @author ThinkPad
 */

public enum OkrResultEnum implements IResultEnumResult {
    /**
     * week template 不可以重复插入数据
     */
    CANNOTINSERTWEEKTEMPLATEDUPLICATE("1001","Cannot insert duplicate data");

    private String code;
    private String message;

    OkrResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
