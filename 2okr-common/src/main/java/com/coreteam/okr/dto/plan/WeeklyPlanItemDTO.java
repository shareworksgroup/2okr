package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


/**
 * @ClassName: WeeklyPlanItemDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 9:11
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanItemDTO {
    private Long id;

    private Long weeklyPlanId;

    /**
     * plan id
     */
    private Long planId;
    /**
     * plan
     */
    private PlanDTO plan;

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

    private Integer commentNum;


}
