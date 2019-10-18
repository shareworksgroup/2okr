package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName: InsertObjectiveDTO
 * @Description create objective dto object
 * @Author jianyong.jiang
 * @Date 2019/3/19 17:09
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertObjectiveDTO {
    /**
     * the name of objective
     */
    @NotNull
    private String name;

    private String desc;

    @NotNull
    private ObjectiveLevelEnum level;

    @NotNull
    private Long organizationId;

    private Long teamId;

    /**
     * the owner of objective
     */
    @NotNull
    private Long ownerId;
    @NotNull
    private Long validatorId;

    @NotNull
    private Date periodStartTime;
    @NotNull
    private Date periodEndTime;

    private String alignment;

    /*private String tags;*/

}
