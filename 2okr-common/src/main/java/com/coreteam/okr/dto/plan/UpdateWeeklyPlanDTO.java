package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: UpdateWeeklyPlanDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 14:09
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateWeeklyPlanDTO {
    private Integer weeklyJobSatisfaction;
}
