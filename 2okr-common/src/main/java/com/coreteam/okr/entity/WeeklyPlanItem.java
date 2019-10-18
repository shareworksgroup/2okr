package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: WeeklyPlanItem
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Data
public class WeeklyPlanItem extends  BaseEntity implements Serializable {
    private Long id;


    private Long weeklyPlanId;

    /**
     * plan id
     */
    private Long planId;

    /**
     * year+week
     */
    private Integer week;

    /**
     * status，plan,done,problem
     */
    private Long categorieId;

    /**
     * 页面显示颜色
     */
    private String color;

    /**
     * plan和problem跨周迁移次数
     */
    private Integer carriedOverAge;

    private Long ownerId;

    private String ownerName;

    private static final long serialVersionUID = 1L;

    public WeeklyPlanItem() {
    }

    public WeeklyPlanItem(Long weeklyPlanId, Long planId, Integer week, Long categorieId, String color, Integer carriedOverAge, Long ownerId, String ownerName) {
        this.weeklyPlanId = weeklyPlanId;
        this.planId = planId;
        this.week = week;
        this.categorieId = categorieId;
        this.color = color;
        this.carriedOverAge = carriedOverAge;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }
}