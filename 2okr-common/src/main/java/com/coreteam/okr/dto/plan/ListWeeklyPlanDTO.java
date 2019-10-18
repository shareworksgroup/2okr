package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ListWeeklyPlanDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 10:47
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListWeeklyPlanDTO {
    private Long userId;
    private Integer week;
    private Long organizationId;

}
