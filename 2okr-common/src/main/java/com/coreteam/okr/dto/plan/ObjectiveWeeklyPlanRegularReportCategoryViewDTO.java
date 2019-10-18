package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ObjectiveWeeklyPlanRegularReportCategoryViewDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/02 14:03
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveWeeklyPlanRegularReportCategoryViewDTO {
    private String reportTime="";
    private String orgAvgProgress="0%";
    private String teamAvgProgress="0%";
    private String memberAvgProgress="0%";
    private List<PersonalWeeklyPlanSimpleReportForRegularReportDTO> memberReportStatics;
    private List<WeeklyTemplateCategoryInfoDTO> categorieList;
    Map<String,List<WeeklyPlanItemViewDTO>> weeklyPlans;
}
