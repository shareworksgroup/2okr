package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * weekly_plan_template
 * @author 
 */
@Data
public class WeeklyPlanTemplate extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    private String templateName;

    /**
     * SYSTEM,ORGANIZATION,TEAM,MEMBER
     */
    private String type;

    private Long entityId;

    /**
     * template is used
     */
    private Byte enable;



    private static final long serialVersionUID = 1L;


}