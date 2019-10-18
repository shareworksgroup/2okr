package com.coreteam.okr.dto.member;

import com.coreteam.okr.constant.MemberRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ChangeTeamMemberRoleDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 16:55
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeTeamMemberRoleDTO {
    private Long teamId;
    private Long userId;
    private MemberRoleEnum role;
}
