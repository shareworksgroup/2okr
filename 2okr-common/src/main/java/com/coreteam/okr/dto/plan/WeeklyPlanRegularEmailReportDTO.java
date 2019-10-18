package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.MemberObjectiveRegularEmailReportDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: WeeklyPlanRegularEmailReportDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/14 9:22
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanRegularEmailReportDTO {
    private MemberObjectiveRegularEmailReportDTO orgOkrs;
    private List<MemberObjectiveRegularEmailReportDTO> teamOkrs;
    private List<WeeklyPlanReportUserDTO> memberWeeklyPlanReports;
}
