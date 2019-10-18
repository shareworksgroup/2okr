package com.coreteam.okr.dto.member;

import com.coreteam.okr.constant.MemberRoleEnum;
import com.coreteam.okr.constant.MemberTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: ChangeMemberRoleDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/15 15:15
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeMemberRoleDTO {
    @NotNull
    private Long entityId;
    @NotNull
    private Long userId;

    private String userName;

    @NotNull
    private MemberRoleEnum role;
    @NotNull
    private MemberTypeEnum type;
}
