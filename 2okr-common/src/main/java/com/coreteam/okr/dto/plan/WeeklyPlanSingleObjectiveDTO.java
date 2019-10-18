package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.ObjectiveConbineWeeklyPlanDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: WeeklyPlanSingleObjectiveDTO
 * @Description 当个的Objective绑定的weeklyplan DTO
 * @Author sean.deng
 * @Date 2019/09/29 9:32
 * @Version 1.0.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanSingleObjectiveDTO {

    private Long id;

    private Integer week;

    private Integer weeklyJobSatisfaction;

    private Long ownerId;

    private String ownerName;

    private Long organizationId;

    private ObjectiveConbineWeeklyPlanDTO weeklyPlans;

}
