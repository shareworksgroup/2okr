package com.coreteam.okr.entity;

import com.coreteam.okr.constant.MemberRoleEnum;
import com.coreteam.okr.constant.MemberStatusEnum;
import com.coreteam.okr.constant.MemberTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * member
 * @author 
 */
@Data
public class Member extends BaseEntity implements Serializable {
    private Long id;

    private Long teamId;

    private Long userId;

    private Long inviteId;

    private String userName;

    private Long organizationId;

    private MemberRoleEnum role;

    private MemberTypeEnum type;

    private MemberStatusEnum status;

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", teamId=").append(teamId);
        sb.append(", userId=").append(userId);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", role=").append(role);
        sb.append(", type=").append(type);
        sb.append(", creatdedAt=").append(this.getCreatedAt());
        sb.append(", updatedAt=").append(this.getUpdatedAt());
        sb.append(", deletedAt=").append(this.getDeletedAt());
        sb.append(", isDeleted=").append(this.getDeleted());
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}