package com.coreteam.okr.dto.member;

import com.coreteam.okr.constant.MemberRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: MemberDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 17:40
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDTO {
    private Long id;

    private Long organizationId;

    private Long teamId;

    private Long userId;

    private String userName;

    private MemberRoleEnum role;

}
