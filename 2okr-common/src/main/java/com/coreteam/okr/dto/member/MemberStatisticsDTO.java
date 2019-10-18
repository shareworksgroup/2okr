package com.coreteam.okr.dto.member;

import com.coreteam.okr.constant.MemberRoleEnum;
import com.coreteam.okr.constant.MemberStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: MemberStatisticsDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 15:54
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberStatisticsDTO {
    private Long id;

    private Long inviteId;

    private Long organizationId;

    private Long teamId;

    private Long userId;

    private String userName;

    private MemberRoleEnum role;

    private MemberStatusEnum status;

    private String name;

    private Integer groupCount;

    private Integer okrNum;
}
