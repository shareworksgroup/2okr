package com.coreteam.okr.dto.plan;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: PersonalWeeklyPlanSimpleReportForRegularReportDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/18 15:30
 * @Version 1.0.0
 */
@Data
public class PersonalWeeklyPlanSimpleReportForRegularReportDTO {
    private String name;
    private List<WeeklyTemplateCategoryInfoDTO> categorieList;
    private Map<Long,Integer> planNum;
    private Integer weeklyJobSatisfaction;
}
