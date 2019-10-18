package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: InsertWeelyPlanItemDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 10:27
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertWeelyPlanItemDTO {
    @NotNull
    private String desc;
    @NotNull
    private Long categorieId;

    private Long objectiveId;
}
