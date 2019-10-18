package com.coreteam.okr.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: InvitationInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/28 11:35
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvitationInfoDTO {
    private Long organizationId;
    private String organizationName;
    private Long teamId;
    private String teamName;
    private Long inviterId;
    private String inviterName;
    private Boolean  existence;
}

