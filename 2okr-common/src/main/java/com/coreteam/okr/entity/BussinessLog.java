package com.coreteam.okr.entity;

import com.coreteam.okr.constant.BussinessLogEntityEnum;
import com.coreteam.okr.constant.BussinessLogOperateTypeEnum;

import java.io.Serializable;

/**
 * bussiness_log
 *
 * @author
 */
public class BussinessLog extends BaseEntity implements Serializable {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BussinessLogOperateTypeEnum getOperateType() {
        return operateType;
    }

    public void setOperateType(BussinessLogOperateTypeEnum operateType) {
        this.operateType = operateType;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getRefEntityId() {
        return refEntityId;
    }

    public void setRefEntityId(Long refEntityId) {
        this.refEntityId = refEntityId;
    }

    public BussinessLogEntityEnum getEntityType() {
        return entityType;
    }

    public void setEntityType(BussinessLogEntityEnum entityType) {
        this.entityType = entityType;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BussinessLog other = (BussinessLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getOperateType() == null ? other.getOperateType() == null : this.getOperateType().equals(other.getOperateType()))
                && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
                && (this.getDesc() == null ? other.getDesc() == null : this.getDesc().equals(other.getDesc()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getOrganizationId() == null ? other.getOrganizationId() == null : this.getOrganizationId().equals(other.getOrganizationId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOperateType() == null) ? 0 : getOperateType().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getDesc() == null) ? 0 : getDesc().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operateType=").append(operateType);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", desc=").append(desc);
        sb.append(", createdAt=").append(this.getCreatedAt());
        sb.append(", updatedAt=").append(this.getUpdatedAt());
        sb.append(", deletedAt=").append(this.getDeletedAt());
        sb.append(", isDeleted=").append(this.getDeleted());
        sb.append(", organizationId=").append(organizationId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    private Long id;

    /**
     * the type of operate
     */
    private BussinessLogOperateTypeEnum operateType;

    /**
     * the operator user id
     */
    private Long operatorId;

    /**
     * description for operate
     */
    private String desc;


    private Long organizationId;

    private Long refEntityId;

    private BussinessLogEntityEnum entityType;

    private static final long serialVersionUID = 1L;
}