package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.ObjectiveBusinessStatusEnum;
import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.coreteam.okr.constant.ObjectiveStatusEnum;
import com.coreteam.okr.dto.Authorization.ObjectivePrivilege;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: ObjectiveDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/29 14:40
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveDTO {

    private Long id;

    private Long linkedObjectiveId;

    private String name;

    private String desc;

    private String groupName;

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

    /*private String tags;*/

    private Long confirmUserId;

    private String confirmUserName;

    private String confirmComment;

    private Long createdUser;

    private String createdName;

    private Date confirmAt;

    private Double confirmFinalResult;

    private Double progress;

    private Double lastProgress;

    private ObjectivePrivilege privilege;

    private List<ObjectiveKeyResultDTO> keyResults;
}
