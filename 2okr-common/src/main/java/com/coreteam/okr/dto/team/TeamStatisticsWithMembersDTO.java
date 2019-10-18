package com.coreteam.okr.dto.team;

import com.coreteam.okr.dto.Authorization.Privilege;
import com.coreteam.okr.dto.member.MemberDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: TeamStatisticsWithMembersDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/28 10:25
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamStatisticsWithMembersDTO {
    private Long id;

    private String name;

    private Long leaderId;

    private String leaderName;

    private Integer organizationId;

    private Integer memberNum;

    private Integer okrNum;

    private Privilege privilege;

    private List<MemberDTO> members;

}
