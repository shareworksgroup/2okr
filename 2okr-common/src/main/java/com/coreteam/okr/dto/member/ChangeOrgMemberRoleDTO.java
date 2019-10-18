package com.coreteam.okr.dto.member;

import com.coreteam.okr.constant.MemberRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: ChangeOrgMemberRoleDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 16:54
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeOrgMemberRoleDTO {
    @NotNull
    private Long organizationId;
    @NotNull
    private Long userId;
    @NotNull
    private MemberRoleEnum role;
}
