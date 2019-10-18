package com.coreteam.okr.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: MemberInvitedDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/23 10:08
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberInvitedDTO {
    @NotNull
    private String info;
}
