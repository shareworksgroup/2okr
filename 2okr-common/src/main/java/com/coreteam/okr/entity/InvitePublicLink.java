package com.coreteam.okr.entity;

import com.coreteam.okr.constant.MemberTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * pulbic_link_invite
 * @author 
 */
@Data
public class InvitePublicLink extends  BaseEntity implements Serializable {
    private Long id;

    private Long organizationId;

    private Long teamId;

    private MemberTypeEnum entityType;

    private Long inviterId;

    private Long expireTime;

    private static final long serialVersionUID = 1L;

}