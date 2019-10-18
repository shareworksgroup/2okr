package com.coreteam.okr.dto.Notify;

import com.coreteam.okr.entity.Invite;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: RegisterInvitedEmailNotifyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 11:31
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterInvitedEmailNotifyDTO {
    private Invite invite;

    public RegisterInvitedEmailNotifyDTO(Invite invite) {
        this.invite = invite;
    }

    public RegisterInvitedEmailNotifyDTO() {
    }
}
