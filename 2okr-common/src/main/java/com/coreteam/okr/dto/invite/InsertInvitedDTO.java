package com.coreteam.okr.dto.invite;

import com.coreteam.okr.constant.InviteBelongEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: InsertInvitedDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/07 17:15
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertInvitedDTO {
    private Long teamId;

    private Long organizationId;

    private String email;

    private InviteBelongEnum type;

    private Long inviterId;

    private String inviterName;

    public InsertInvitedDTO() {
    }

    public InsertInvitedDTO(Long teamId, Long organizationId, String email, InviteBelongEnum type, Long inviterId, String inviterName) {
        this.teamId = teamId;
        this.organizationId = organizationId;
        this.email = email;
        this.type = type;
        this.inviterId = inviterId;
        this.inviterName = inviterName;
    }
}
