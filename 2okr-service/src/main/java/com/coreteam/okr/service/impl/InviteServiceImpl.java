package com.coreteam.okr.service.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.constant.*;
import com.coreteam.okr.dao.InviteDAO;
import com.coreteam.okr.dao.MemberDAO;
import com.coreteam.okr.dao.OrganizationDAO;
import com.coreteam.okr.dao.TeamDAO;
import com.coreteam.okr.dto.Notify.MemberInvitedEmailNotifyDTO;
import com.coreteam.okr.dto.Notify.RegisterInvitedEmailNotifyDTO;
import com.coreteam.okr.dto.invite.InsertInvitedDTO;
import com.coreteam.okr.dto.member.InsertMemberDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Invite;
import com.coreteam.okr.entity.Member;
import com.coreteam.okr.entity.Organization;
import com.coreteam.okr.entity.Team;
import com.coreteam.okr.manager.BasicMemberManager;
import com.coreteam.okr.manager.Notify;
import com.coreteam.okr.manager.NotifyManager;
import com.coreteam.okr.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: InviteServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 11:31
 * @Version 1.0.0
 */
@Service
@Slf4j
public class InviteServiceImpl implements InviteService {




    @Override
    @Transactional
    public void acceptInvited(Long invitedId) {
        Invite invited = this.inviteDAO.selectByPrimaryKey(invitedId);
        if (invited == null) {
            throw new CustomerException("the invited is not exist,id:" + invitedId);
        }
        //已经接受
        if (invited.getAccept()) {
            return;
        }
        Member member = this.memberDAO.findMemberByInvitedId(invited.getId());
        if (member != null) {
            member.setStatus(MemberStatusEnum.NORMAL);
            member.initializeForUpdate();
            this.memberDAO.updateByPrimaryKeySelective(member);
            //如果team邀请，同时将user添加为org的成员
            if (member.getType() == MemberTypeEnum.TEAM) {
                Member orgMember = memberManager.checkMemberExistIgnoreStatus(member.getOrganizationId(), member.getUserId(), null, MemberTypeEnum.ORGANIZATION);
                if(orgMember!=null){
                    if(orgMember.getStatus()!=MemberStatusEnum.NORMAL){
                        orgMember.setStatus(MemberStatusEnum.NORMAL);
                        orgMember.initializeForUpdate();
                        memberDAO.updateByPrimaryKeySelective(orgMember);
                    }
                }else{
                    InsertMemberDTO insetDTO=new InsertMemberDTO();
                    insetDTO.setOrganizationId(member.getOrganizationId());
                    insetDTO.setUserName(member.getUserName());
                    insetDTO.setType(MemberTypeEnum.ORGANIZATION);
                    insetDTO.setUserId(member.getUserId());
                    memberManager.insertMember(insetDTO,MemberStatusEnum.NORMAL);
                }
            }
        }
        invited.setAccept(true);
        invited.initializeForUpdate();
        this.inviteDAO.updateByPrimaryKeySelective(invited);
    }


    @Override
    @Transactional
    public void joinTheTeamAfterRegister(Invite invite, UserInfoDTO userInfo) {
        try {
            //在完成注册后加入到对应的组织
            Member member = this.memberDAO.findMemberByInvitedId(invite.getId());
            member.setUserId(userInfo.getId());
            member.setUserName(userInfo.getName());
            member.setStatus(MemberStatusEnum.NORMAL);
            this.memberDAO.updateByPrimaryKeySelective(member);
            //如果team邀请，同时将user添加为org的成员
            if (member.getType() == MemberTypeEnum.TEAM) {
                Member orgMember = memberManager.checkMemberExistIgnoreStatus(member.getOrganizationId(), userInfo.getId(), null, MemberTypeEnum.ORGANIZATION);
                orgMember=orgMember!=null?orgMember:memberManager.checkMemberExistIgnoreStatus(member.getOrganizationId(), null,userInfo.getEmail(), MemberTypeEnum.ORGANIZATION);
                if(orgMember!=null){
                    if(orgMember.getStatus()!=MemberStatusEnum.NORMAL){
                        orgMember.setStatus(MemberStatusEnum.NORMAL);
                        orgMember.initializeForUpdate();
                        memberDAO.updateByPrimaryKeySelective(orgMember);
                    }
                }else{
                    InsertMemberDTO insetDTO=new InsertMemberDTO();
                    insetDTO.setOrganizationId(member.getOrganizationId());
                    insetDTO.setUserName(member.getUserName());
                    insetDTO.setType(MemberTypeEnum.ORGANIZATION);
                    insetDTO.setUserId(member.getUserId());
                    memberManager.insertMember(insetDTO,MemberStatusEnum.NORMAL);
                }
            }
            invite.setAccept(true);
            invite.initializeForUpdate();
            this.inviteDAO.updateByPrimaryKeySelective(invite);
        } catch (Exception e) {

        }
    }

    @Override
    @Transactional
    public Long insertAndInvitedRegister(InsertInvitedDTO insertInvitedDTO) {
        Invite invite = new Invite();
        BeanConvertUtils.copyEntityProperties(insertInvitedDTO, invite);
        invite.setRetryTimes(0);
        invite.setAccept(false);
        invite.setSended(true);
        invite.setInviteType(InviteTypeEnum.REGISTER);
        invite.initializeForInsert();
        inviteDAO.insert(invite);
        sendRegisterInvitedMessge(invite);
        return invite.getId();
    }

    @Override
    public Long insertAndInvitedJoin(InsertInvitedDTO insertInvitedDTO, UserInfoDTO toUser) {
        String groupName = "";
        if (insertInvitedDTO.getType() == InviteBelongEnum.ORGANIZATION) {
            Organization org = this.organizationDAO.selectByPrimaryKey(insertInvitedDTO.getOrganizationId());
            groupName = org.getName();
        } else {
            Team team = teamDAO.selectByPrimaryKey(insertInvitedDTO.getTeamId());
            if (team != null) {
                groupName = team.getName();
            }
        }
        Invite invite = new Invite();
        BeanConvertUtils.copyEntityProperties(insertInvitedDTO, invite);
        invite.setRetryTimes(0);
        invite.setAccept(false);
        invite.setSended(true);
        invite.setInviteType(InviteTypeEnum.JOIN);
        invite.initializeForInsert();
        inviteDAO.insert(invite);
        sendMemberJoinInvitedMessage(invite,toUser.getName(),groupName);
        return invite.getId();
    }

    @Override
    public void reSendInvitedMessage(Long invitedId) {
        Invite invited=this.inviteDAO.selectByPrimaryKey(invitedId);
        if(invited==null){
            throw new CustomerException(" the invited not exist");
        }
        if(!organizationService.hasPermission(invited.getOrganizationId(),PrivilegeTypeEnum.UPDATABLE)){
            if(invited.getType()==InviteBelongEnum.TEAM){
                if(!teamService.hasPermission(invited.getTeamId(),PrivilegeTypeEnum.UPDATABLE)){
                    throw new CustomerException("has no permission,to insert member must be owner");
                }
            }else{
                throw new CustomerException("has no permission,to insert member must be owner");
            }
        }


        if(invited.getInviteType()==InviteTypeEnum.REGISTER){
            //重新发送注册邀请
            sendRegisterInvitedMessge(invited);
            invited.setSended(true);
            invited.setRetryTimes(invited.getRetryTimes()+1);
            invited.initializeForUpdate();
            this.inviteDAO.updateByPrimaryKeySelective(invited);
        }else{
            UserInfoDTO invitedUser = userService.getUserInfoByEmail(invited.getEmail());
            String groupName = "";
            if (invited.getType() == InviteBelongEnum.ORGANIZATION) {
                Organization org = this.organizationDAO.selectByPrimaryKey(invited.getOrganizationId());
                groupName = org.getName();
            } else {
                Team team = teamDAO.selectByPrimaryKey(invited.getTeamId());
                if (team != null) {
                    groupName = team.getName();
                }
            }
            sendMemberJoinInvitedMessage(invited,invitedUser.getName(),groupName);
            invited.setSended(true);
            invited.setRetryTimes(invited.getRetryTimes()+1);
            invited.initializeForUpdate();
            this.inviteDAO.updateByPrimaryKeySelective(invited);
        }

    }


    @Override
    public List<Invite> listRegisterInvitedEmail(String email) {
        return this.inviteDAO.listRegisterInvitedEmail(email);
    }

    private void sendRegisterInvitedMessge(Invite invited){
        notifyManager.sendNotify(new Notify<RegisterInvitedEmailNotifyDTO>() {
            @Override
            public NotifyTypeEnum type() {
                return NotifyTypeEnum.EMAIL ;
            }

            @Override
            public RegisterInvitedEmailNotifyDTO message() {
                return new RegisterInvitedEmailNotifyDTO(invited);
            }

            @Override
            public NotifyBusinessType businessType() {
                return NotifyBusinessType.EMAIL_REGISTER;
            }
        });
    }

    private void sendMemberJoinInvitedMessage(Invite invited,String userName,String groupName){
        final String name=groupName;
        notifyManager.sendNotify(new Notify<MemberInvitedEmailNotifyDTO>() {
            @Override
            public NotifyTypeEnum type() {
                return NotifyTypeEnum.EMAIL;
            }

            @Override
            public MemberInvitedEmailNotifyDTO message() {
                return new MemberInvitedEmailNotifyDTO(invited, userName, name);
            }

            @Override
            public NotifyBusinessType businessType() {
                return NotifyBusinessType.EMAIL_JOIN;
            }
        });
    }



    @Autowired
    private InviteDAO inviteDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private BasicMemberManager memberManager;

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private MemberService memberService;

    @Autowired
    private NotifyManager notifyManager;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TeamService teamService;
}
