package com.coreteam.okr.dto.plan;

import lombok.Data;

import java.util.Set;

@Data
public class SendOrgWeeklyPlanReportDTO {

    private Long orgid;

    private Long userid;

    private String userName;

    private Integer week;

    private Set<String> Email;
}
