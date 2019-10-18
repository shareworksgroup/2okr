package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: WeeklyPlan
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Data
public class WeeklyPlan extends BaseEntity implements Serializable {

    private Long id;

    /**
     * year+week
     */
    private Integer week;

    /**
     * 自我评分1-5
     */
    private Integer weeklyJobSatisfaction;

    private Long ownerId;

    private String ownerName;
    private Long organizationId;
    private Long templateId;


    private static final long serialVersionUID = 1L;

    public WeeklyPlan(Long ownerId, String ownerName, Integer week, Integer weeklyJobSatisfaction,Long organizationId,Long templateId) {
        this.week = week;
        this.weeklyJobSatisfaction = weeklyJobSatisfaction;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.organizationId=organizationId;
        this.templateId=templateId;
    }

    public WeeklyPlan() {

    }
}