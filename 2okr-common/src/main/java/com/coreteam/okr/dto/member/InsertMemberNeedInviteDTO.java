package com.coreteam.okr.dto.member;

import com.coreteam.okr.constant.MemberTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: InsertMemberNeedInviteDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/07 16:59
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertMemberNeedInviteDTO {
    @NotNull
    private Long organizationId;

    private Long teamId;

    @NotNull
    private String email;

    @NotNull
    private MemberTypeEnum type;

    public InsertMemberNeedInviteDTO() {
    }

    public InsertMemberNeedInviteDTO( Long organizationId, Long teamId,  String email, MemberTypeEnum type) {
        this.organizationId = organizationId;
        this.teamId = teamId;
        this.email = email;
        this.type = type;
    }
}
