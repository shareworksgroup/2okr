package com.coreteam.okr.dto.plantemplate;

import lombok.Data;

/**
 * @ClassName: WeeklyPlanTemplateItemDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/17 13:41
 * @Version 1.0.0
 */
@Data
public class WeeklyPlanTemplateItemDTO {

    private Long id;

    private String name;

    private Long templateId;

    private Integer order;

    private Boolean needCarry;

    private String color;

}
