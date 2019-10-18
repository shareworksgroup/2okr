package com.coreteam.okr.entity;

import com.coreteam.okr.constant.ObjectiveBusinessStatusEnum;
import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.coreteam.okr.constant.ObjectiveStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * objective
 * @author 
 */
@Data
public class Objective extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    private Long linkedObjectiveId;

    private String name;

    private String desc;

    /**
     * Organization
     */
    private Long organizationId;

    private Long teamId;

    private ObjectiveStatusEnum status;

    private ObjectiveBusinessStatusEnum businessStatus;

    private ObjectiveLevelEnum level;

    private Long ownerId;

    private String ownerName;

    private Long validatorId;

    private String validatorName;

    private Date periodStartTime;

    private Date periodEndTime;

    private String alignment;

    private String tags;

    private Long confirmUserId;

    private String confirmUserName;

    private Long createdUser;

    private String createdName;

    private String confirmComment;

    private Date confirmAt;

    private Double confirmFinalResult;

    private Double progress;

    private Double lastProgress;

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        return "Objective{" +
                "id=" + id +
                ", linkedObjectiveId=" + linkedObjectiveId +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", organizationId=" + organizationId +
                ", teamId=" + teamId +
                ", status=" + status +
                ", businessStatus=" + businessStatus +
                ", level=" + level +
                ", ownerId=" + ownerId +
                ", ownerName='" + ownerName + '\'' +
                ", validatorId=" + validatorId +
                ", validatorName=" + validatorName +
                ", periodStartTime=" + periodStartTime +
                ", periodEndTime=" + periodEndTime +
                ", alignment='" + alignment + '\'' +
                ", tags='" + tags + '\'' +
                ", confirmUserId=" + confirmUserId +
                ", confirmUserName='" + confirmUserName + '\'' +
                ", confirmComment='" + confirmComment + '\'' +
                ", confirmAt=" + confirmAt +
                ", confirmFinalResult=" + confirmFinalResult +
                ", progress=" + progress +
                '}';
    }

}