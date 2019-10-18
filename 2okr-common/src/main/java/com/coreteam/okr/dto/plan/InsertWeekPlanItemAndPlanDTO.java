package com.coreteam.okr.dto.plan;

import com.coreteam.okr.constant.PlanLeveEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: InsertWeekPlanItemAndPlanDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 13:41
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertWeekPlanItemAndPlanDTO {

    private Long weeklyPlanId;
    private Integer week;
    private Long ownerId;
    private String ownerName;

    private String desc;
    private PlanLeveEnum level;
    private Long organizationId;
    private Long teamId;

    private Long categorieId;

    public InsertWeekPlanItemAndPlanDTO() {
    }

    public InsertWeekPlanItemAndPlanDTO(Long weeklyPlanId, Integer week, Long ownerId, String ownerName, String desc, PlanLeveEnum level, Long organizationId, Long teamId,Long categorieId) {
        this.weeklyPlanId = weeklyPlanId;
        this.week = week;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.desc = desc;
        this.level = level;
        this.organizationId = organizationId;
        this.teamId = teamId;
        this.categorieId=categorieId;
    }
}
