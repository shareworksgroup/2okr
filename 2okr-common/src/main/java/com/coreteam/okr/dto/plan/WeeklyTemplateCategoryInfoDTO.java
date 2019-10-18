package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: WeeklyTemplateCategoryInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/17 19:18
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyTemplateCategoryInfoDTO {
    private Long id;
    private String name;
    private String color;
    private Integer order;
}
