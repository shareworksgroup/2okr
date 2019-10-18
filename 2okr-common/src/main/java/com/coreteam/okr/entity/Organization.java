package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * organization
 * @author 
 */
@Data
public class Organization extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    /**
     * the name of organization
     */
    private String name;

    /**
     * the email of organization
     */
    private String email;

    /**
     * organization description
     */
    private String desc;

    /**
     * organization show color
     */
    private String color;

    private Long managerId;

    private String managerName;

    private Long createdUser;

    private String createdName;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", email=").append(email);
        sb.append(", desc=").append(desc);
        sb.append(", color=").append(color);
        sb.append(", createdAt=").append(this.getCreatedAt());
        sb.append(", updatedAt=").append(this.getUpdatedAt());
        sb.append(", deletedAt=").append(this.getDeletedAt());
        sb.append(", isDeleted=").append(this.getDeleted());
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}