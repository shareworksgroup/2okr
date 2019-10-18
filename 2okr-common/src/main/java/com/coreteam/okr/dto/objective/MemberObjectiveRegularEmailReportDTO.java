package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: MemberObjectiveRegularEmailReportDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/13 11:05
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberObjectiveRegularEmailReportDTO {
    private String name;
    private Integer okrNum;
    private Integer krNum;
    private Double  avgProgress;
    List<ObjectiveConbineWeeklyPlanDTO> okrs;
}
