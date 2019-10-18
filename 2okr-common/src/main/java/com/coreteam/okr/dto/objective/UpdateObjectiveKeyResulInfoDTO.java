package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: UpdateObjectiveKeyResulInfoDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/4/1
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateObjectiveKeyResulInfoDTO {
    @NotNull
    private Long id;
    /**
     * the key result's name
     */
    private String name;

    /**
     * key result 描述
     */
    private String desc;

    private String metricUnit;

    private Long ownerId;

    private String ownerName;

    private Integer confidenceLevel;

    @NotNull
    private Long objectiveId;
}
