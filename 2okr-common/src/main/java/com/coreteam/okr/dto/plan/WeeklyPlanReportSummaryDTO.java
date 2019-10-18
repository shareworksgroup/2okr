package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: WeeklyPlanReportSummaryDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 17:44
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanReportSummaryDTO {
    private String  date;
    private Double avgFeedBack;
    private Integer issuesNum;
    private Integer completionNum;
    private Integer totalNum;
}
