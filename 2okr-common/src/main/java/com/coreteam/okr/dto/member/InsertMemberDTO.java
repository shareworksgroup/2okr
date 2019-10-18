package com.coreteam.okr.dto.member;

import com.coreteam.okr.constant.MemberTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: InsertMemberDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 15:51
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertMemberDTO {
    @NotNull
    private Long organizationId;

    private Long teamId;

    @NotNull
    private Long userId;

    @NotNull
    private String userName;

    @NotNull
    private MemberTypeEnum type;

    private Long inviteId;

    public InsertMemberDTO() {

    }

    public InsertMemberDTO(Long organizationId, Long teamId,Long userId, String userName, MemberTypeEnum type,Long inviteId) {
        this.organizationId = organizationId;
        this.teamId = teamId;
        this.userId = userId;
        this.userName = userName;
        this.type = type;
        this.inviteId=inviteId;
    }
}
