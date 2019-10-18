package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeeklyPlanReportUserDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 16:17
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanReportUserDTO {

    private Long weeklyPlanId;

    private Integer week;

    private Integer weeklyJobSatisfaction;

    private Long ownerId;

    private String ownerName;

    private List<WeeklyTemplateCategoryInfoDTO> categorieList;

    Map<String,List<WeeklyPlanItemViewDTO>> itemMap;

}
