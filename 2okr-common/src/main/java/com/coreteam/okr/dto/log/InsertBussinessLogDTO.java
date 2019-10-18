package com.coreteam.okr.dto.log;

import com.coreteam.okr.constant.BussinessLogEntityEnum;
import com.coreteam.okr.constant.BussinessLogOperateTypeEnum;

/**
 * @ClassName: InsertBussinessLogDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/25 14:34
 * @Version 1.0.0
 */
public class InsertBussinessLogDTO {
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
}
