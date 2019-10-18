package com.coreteam.okr.entity;

import com.coreteam.okr.constant.MemberInvitationJoinType;
import lombok.Data;

import java.io.Serializable;

/**
 * member_invitation_join_record
 * @author 
 */
@Data
public class MemberInvitationJoinRecord  extends  BaseEntity implements Serializable {
    private Long id;

    private Long invitationId;

    private Long memberId;

    private MemberInvitationJoinType inviationType;

    private static final long serialVersionUID = 1L;

}