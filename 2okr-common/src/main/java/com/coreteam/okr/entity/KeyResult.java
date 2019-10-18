package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * key_result
 * @author 
 */
@Data
public class KeyResult extends BaseEntity implements Serializable {
    private Long id;

    /**
     * the key result's name
     */
    private String name;

    /**
     * key result 描述
     */
    private String desc;

    /**
     * 计量单位
     */
    private String metricUnit;

    /**
     * 初始值
     */

    private Double metricStartValue;

    /**
     * 结束值
     */
    private Double metricEndValue;


    /**
     * 当前值
     */
    private Double value;

    /**
     * 当前进度
     */
    private Double progress;


    private Double lastProgress;

    /**
     * 当前keyresult的权重
     */
    private Double weight;

    private Long ownerId;

    private String ownerName;

    private Long objectiveId;

    private Long updateUserId;

    private String updateUserName;

    private Long createdUser;

    private String createdName;

    private Integer confidenceLevel;

    private Double midTermRating;

    private Double finalRating;

    private String summary;


    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", desc=").append(desc);
        sb.append(", metricUnit=").append(metricUnit);
        sb.append(", metricStartValue=").append(metricStartValue);
        sb.append(", metricEndValue=").append(metricEndValue);
        sb.append(", weight=").append(weight);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", objectiveId=").append(objectiveId);
        sb.append(", createdAt=").append(this.getCreatedAt());
        sb.append(", updatedAt=").append(this.getUpdatedAt());
        sb.append(", deletedAt=").append(this.getDeletedAt());
        sb.append(", isDeleted=").append(this.getDeleted());
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}