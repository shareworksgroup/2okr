package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.ObjectiveBusinessStatusEnum;
import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.coreteam.okr.constant.ObjectiveStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: ObjectiveNotConbineKRDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 13:38
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveNotConbineKRDTO {
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


    private Long confirmUserId;

    private String confirmUserName;

    private String confirmComment;

    private Date confirmAt;

    private Double confirmFinalResult;

    private Double progress;
}
