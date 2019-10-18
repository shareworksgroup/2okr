package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeeklyPlanReportCategoryDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 17:07
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanReportCategoryDTO {
    private List<WeeklyTemplateCategoryInfoDTO> categorieList;
    Map<String,List<WeeklyPlanItemViewDTO>> itemMap;
}
