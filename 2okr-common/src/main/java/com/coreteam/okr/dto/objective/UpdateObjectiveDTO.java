package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @ClassName: UpdateObjectiveDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/22 9:21
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateObjectiveDTO {
    @NotNull
    private Long id;

    private String name;

    private String desc;

    private Long teamId;

    private Long ownerId;

    private String ownerName;

    private Long validatorId;

    private String validatorName;

    private String alignment;

    private ObjectiveLevelEnum level;

   /* private String tags;*/





}
