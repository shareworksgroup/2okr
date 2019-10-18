package com.coreteam.okr.constant;

import com.coreteam.core.result.IResultEnumResult;

/**
 * validate error message enum
 *
 * @author jianyong.jiang
 * @date 2019/03/17
 */
public enum ValidateErrorMessageEnum implements IResultEnumResult {
    // organization
    ORGANIZATION_NAME_NOT_REPEAT("Or0001", "organization name cannot be repeated."),
    ORGANIZATION_NOT_EXIST("Or0002", "organization doesn't exists."),

    // team
    TEAM_NAME_NOT_REPEAT("T0001", "team name cannot be repeated."),
    TEAM_ID_NOT_EXISTS("T0002", "team id doesn't exist."),

    //member
    TEAM_MEMBER_NOT_EXIST("M0001", "team member doesn't exist."),
    TEAM_MEMBER_EXIST("M0002", "team member has existed."),
    ORGANIZATION_MEMBER_EXIST("M0002", "organization member has existed."),
    TEAM_MEMBER_DUPLICATE("M0003", "team member was duplicated."),
    PUBLIC_LINK_ACCEPT_ERROR("M0005","member invited accept error."),

    INVITE_MEMBER_ORGANIZATION_ID_NULL("M0004", "invite member organization id cannot be null."),
    INVITE_MEMBER_TEAM_ID_NULL("M0004", "invite member team id cannot be null."),

    OBJECTIVE_ID_NOT_EXIST("Ob001", "can not find objective for special id."),
    OBJECTIVE_STATUS_ERROR_NOT_OPEN("Ob002", "the objective cannot be reopened."),
    OBJECTIVE_STATUS_ERROR_NOT_CLOSE("Ob003", "the objective cannot be closed."),

    OBJECTIVE_KEY_RESULT_ID_NOT_EXIST("Ob004", "can not find objective key result for special id."),

    OBJECTIVE_KEY_RESULT_VALUE_ILLEGAL("Ob005", "the value of key result should between start and end.");

    ValidateErrorMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }

    private String code;
    private String message;
}
