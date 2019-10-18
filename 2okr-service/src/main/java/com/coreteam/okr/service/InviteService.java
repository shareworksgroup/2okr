package com.coreteam.okr.service;

import com.coreteam.okr.dto.invite.InsertInvitedDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Invite;

import java.util.List;

/**
 * @ClassName: InviteService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 11:31
 * @Version 1.0.0
 */
public interface InviteService {

    /**
     * 添加邀请的member
     *
     * @param invitedId
     */
    void acceptInvited(Long invitedId);

    /**
     * 获取未由于未注册而未发送邀请的邀请列表
     *
     * @param email
     * @return
     */
    List<Invite> listRegisterInvitedEmail(String email);

    /**
     * 重新发送邀请确认邮件
     *
     * @param invite
     * @param userInfo
     */
    void joinTheTeamAfterRegister(Invite invite, UserInfoDTO userInfo);

    /**
     * 添加邀请，并且邮件发送注册
     * @param insertInvitedDTO
     */
    Long insertAndInvitedRegister(InsertInvitedDTO insertInvitedDTO);

    /**
     * 添加邀请，并且邮件发送加入
     * @param insertInvitedDTO
     * @param userInfo
     */
    Long insertAndInvitedJoin(InsertInvitedDTO insertInvitedDTO, UserInfoDTO userInfo);

    /**
     * 重新发送邀请邮件
     * @param invitedId
     */
    void reSendInvitedMessage(Long invitedId);
}
