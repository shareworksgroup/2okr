package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.ObjectiveConbineWeeklyPlanDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeeklyPlanReportObjectiveDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 17:25
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanReportObjectiveDTO {
    private List<ObjectiveConbineWeeklyPlanDTO> orgOkrs;
    private List<ObjectiveConbineWeeklyPlanDTO> teamOkrs;
    private List<ObjectiveConbineWeeklyPlanDTO> memberOkrs;
    private List<WeeklyTemplateCategoryInfoDTO> categorieList;
    Map<String,List<WeeklyPlanItemViewDTO>> unLinkItemMap;

}
