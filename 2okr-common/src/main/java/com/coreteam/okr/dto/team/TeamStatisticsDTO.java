package com.coreteam.okr.dto.team;

import com.coreteam.okr.dto.Authorization.Privilege;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: TeamStatisticsDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 17:59
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamStatisticsDTO {
    private Long id;

    private String name;

    private Long leaderId;

    private String leaderName;

    private Integer organizationId;

    private Integer memberNum;

    private Integer okrNum;

    private Privilege privilege;
}
