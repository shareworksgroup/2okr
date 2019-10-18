package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * weekly_plan_template_item
 * @author
 */
@Data
public class WeeklyPlanTemplateItem extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    private String name;

    private Long templateId;

    private Integer order;

    private String color;

    private Boolean needCarry;

    private static final long serialVersionUID = 1L;

}
