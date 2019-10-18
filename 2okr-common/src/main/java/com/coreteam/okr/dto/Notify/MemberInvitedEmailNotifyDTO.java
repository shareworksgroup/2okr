package com.coreteam.okr.dto.Notify;

import com.coreteam.okr.entity.Invite;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: MemberInvitedEmailNotifyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 11:32
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberInvitedEmailNotifyDTO {
    private Invite invite;
    private String toUser;
    private String groupName;

    public MemberInvitedEmailNotifyDTO(Invite invite, String toUser, String groupName) {
        this.invite = invite;
        this.toUser = toUser;
        this.groupName = groupName;
    }

    public MemberInvitedEmailNotifyDTO() {
    }
}
