package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: WeeklyPlanReportReciverDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 12:40
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanReportReciverDTO {
    private List<Long> members;
    private List<String> externalReciver;
    private Boolean attachPdf;
}
