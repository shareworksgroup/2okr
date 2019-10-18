package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * team
 *
 * @author
 */
@Data
public class Team extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    private String name;

    private Long leaderId;

    private String leaderName;

    private Long createdUser;

    private String createdName;

    private Long organizationId;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", leaderId=").append(leaderId);
        sb.append(", createdAt=").append(this.getCreatedAt());
        sb.append(", organizationId=").append(organizationId);
        sb.append(", updatedAt=").append(this.getUpdatedAt());
        sb.append(", deletedAt=").append(this.getDeletedAt());
        sb.append(", isDeleted=").append(this.getDeleted());
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}