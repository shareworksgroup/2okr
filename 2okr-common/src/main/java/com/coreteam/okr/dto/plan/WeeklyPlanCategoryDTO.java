package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeeklyPlanCategoryDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 13:33
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanCategoryDTO {
    private Long id;

    private Integer week;

    private Integer weeklyJobSatisfaction;

    private Long ownerId;

    private String ownerName;

    private Long organizationId;

    private List<WeeklyTemplateCategoryInfoDTO> categorieList;

    Map<String,List<WeeklyPlanItemViewDTO>> itemMap;

}
