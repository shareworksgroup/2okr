package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: WeeklyPlanReportReciverSettingDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/03 9:06
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanReportReciverSettingDTO {
    private List<Long> reciverId;
}
