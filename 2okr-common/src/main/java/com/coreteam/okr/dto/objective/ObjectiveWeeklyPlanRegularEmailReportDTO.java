package com.coreteam.okr.dto.objective;

import com.coreteam.okr.dto.plan.PersonalWeeklyPlanSimpleReportForRegularReportDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanItemViewDTO;
import com.coreteam.okr.dto.plan.WeeklyTemplateCategoryInfoDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ObjectiveWeeklyPlanRegularEmailReportDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/13 10:54
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveWeeklyPlanRegularEmailReportDTO {
    private String userName="";
    private String reportTime="";
    private String orgAvgProgress="0%";
    private String teamAvgProgress="0%";
    private String memberAvgProgress="0%";
    private String reportUrl="";
    private List<PersonalWeeklyPlanSimpleReportForRegularReportDTO> memberReportStatics;
    private MemberObjectiveRegularEmailReportDTO orgOkrs;
    private List<MemberObjectiveRegularEmailReportDTO> teamOkrs;
    private List<MemberObjectiveRegularEmailReportDTO> memberOkrs;
    private List<WeeklyTemplateCategoryInfoDTO> categorieList;
    Map<String,List<WeeklyPlanItemViewDTO>> unLinkItemMap;
}
