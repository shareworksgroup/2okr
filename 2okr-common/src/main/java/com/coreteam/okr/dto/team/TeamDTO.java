package com.coreteam.okr.dto.team;

import com.coreteam.okr.dto.Authorization.Privilege;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: TeamDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 10:37
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamDTO {
    private Long id;

    private String name;

    private Long leaderId;

    private String leaderName;

    private Long organizationId;

    private Integer memberNum;

    private Privilege privilege;
}
