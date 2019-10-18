package com.coreteam.okr.entity;

import com.coreteam.okr.constant.InviteBelongEnum;
import com.coreteam.okr.constant.InviteTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * invite
 *
 * @author
 */
@Data
public class Invite extends BaseEntity implements Serializable {
    private Long id;

    private Long teamId;

    private Long organizationId;

    private String email;

    private InviteBelongEnum type;

    private Boolean accept;

    private Boolean sended;

    private Integer retryTimes;

    private Long inviterId;

    private String inviterName;

    private InviteTypeEnum inviteType;

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", teamId=").append(teamId);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", email=").append(email);
        sb.append(", createdAt=").append(this.getCreatedAt());
        sb.append(", updatedAt=").append(this.getUpdatedAt());
        sb.append(", deletedAt=").append(this.getDeletedAt());
        sb.append(", isDeleted=").append(this.getDeleted());
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}