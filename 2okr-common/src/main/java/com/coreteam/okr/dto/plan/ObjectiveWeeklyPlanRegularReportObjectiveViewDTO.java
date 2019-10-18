package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.MemberObjectiveRegularEmailReportDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ObjectiveWeeklyPlanRegularReportObjectiveViewDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/02 13:58
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveWeeklyPlanRegularReportObjectiveViewDTO {
    private String reportTime="";
    private String orgAvgProgress="0%";
    private String teamAvgProgress="0%";
    private String memberAvgProgress="0%";
    private List<PersonalWeeklyPlanSimpleReportForRegularReportDTO> memberReportStatics;
    private MemberObjectiveRegularEmailReportDTO orgOkrs;
    private List<MemberObjectiveRegularEmailReportDTO> teamOkrs;
    private List<MemberObjectiveRegularEmailReportDTO> memberOkrs;
    private List<WeeklyTemplateCategoryInfoDTO> categorieList;
    Map<String,List<WeeklyPlanItemViewDTO>> unLinkItemMap;
}
