package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * weekly_plan_item_dalily_status
 * @author 
 */
@Data
public class WeeklyPlanItemDalilyStatus extends BaseEntity implements Serializable {

    private Long id;

    private Long weeklyPlanId;

    private Long weeklyPlanItemId;

    private String date;

    private Integer week;

    private Long  categorieId;

    private Long templateId;

    private Integer jobStatisfaction;

    private Long ownerId;

    private String ownerName;

    private Long organizationId;

    private static final long serialVersionUID = 1L;

    public WeeklyPlanItemDalilyStatus(Long weeklyPlanId, Long weeklyPlanItemId, String date, Long categorieId, Long ownerId, String ownerName,Integer week) {
        this.weeklyPlanId=weeklyPlanId;
        this.weeklyPlanItemId = weeklyPlanItemId;
        this.date = date;
        this.categorieId = categorieId;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.week=week;
    }

    public WeeklyPlanItemDalilyStatus() {
    }
}

