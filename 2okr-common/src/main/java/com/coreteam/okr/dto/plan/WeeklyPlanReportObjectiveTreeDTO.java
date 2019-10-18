package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.ObjectiveConbineWeeklyPlanTreeNodeDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeeklyPlanReportObjectiveTreeDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/15 18:02
 * @Version 1.0.0
 */
@Data
public class WeeklyPlanReportObjectiveTreeDTO {
    private List<ObjectiveConbineWeeklyPlanTreeNodeDTO> okrs;
    private List<WeeklyTemplateCategoryInfoDTO> categorieList;
    Map<String,List<WeeklyPlanItemViewDTO>> unLinkItemMap;
}
