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
public class WeeklyReportSendRecord extends BaseEntity implements Serializable {
    private Long id;

    /**
     * year+week
     */
    private Integer week;

    /**
     * 自我评分1-5
     */
    private Long receiverId;

    private Long senderId;

    private Long weeklyPlanId;

    private static final long serialVersionUID = 1L;

    public WeeklyReportSendRecord() {
    }

    public WeeklyReportSendRecord(Integer week, Long receiverId, Long senderId, Long weeklyPlanId) {
        this.week = week;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.weeklyPlanId = weeklyPlanId;
    }
}