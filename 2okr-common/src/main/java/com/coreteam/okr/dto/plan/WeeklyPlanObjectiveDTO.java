package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.ObjectiveConbineWeeklyPlanDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeeklyPlanObjectiveDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 14:16
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanObjectiveDTO {
    private Long id;

    private Integer week;

    private Integer weeklyJobSatisfaction;

    private Long ownerId;

    private String ownerName;

    private Long organizationId;

    private List<ObjectiveConbineWeeklyPlanDTO> okrItems;

    private List<WeeklyTemplateCategoryInfoDTO> categorieList;

    Map<String,List<WeeklyPlanItemViewDTO>> unlinkItemMap;





}
