package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: InsertObjectiveKeyResultDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/29 15:21
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertObjectiveKeyResultDTO {
    @NotNull
    private String name;
    @NotNull
    private String desc;
    @NotNull
    private String metricUnit;
    @NotNull
    private Double metricStartValue;
    @NotNull
    private Double metricEndValue;
    @NotNull
    private Double weight;
    @NotNull
    private Long ownerId;
    @NotNull
    private Long objectiveId;

    @Min(value = 0)
    @Max(value = 100)
    private Integer confidenceLevel;
}
