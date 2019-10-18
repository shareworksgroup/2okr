package com.coreteam.okr.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.result.IResultEnumResult;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.common.util.AESUtil;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.constant.*;
import com.coreteam.okr.dao.*;
import com.coreteam.okr.dto.invite.InsertInvitedDTO;
import com.coreteam.okr.dto.invite.InviteMemberDTO;
import com.coreteam.okr.dto.member.*;
import com.coreteam.okr.dto.team.TeamStatisticsDTO;
import com.coreteam.okr.dto.user.SimpleUserInfoDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.*;
import com.coreteam.okr.manager.BasicMemberManager;
import com.coreteam.okr.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName: MemberServiceImpl
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 16:36
 * @Version 1.0.0
 */
@Service
@Slf4j
public class MemberServiceImpl extends BaseService implements MemberService {

    @Override
    public void inviteMembers(InviteMemberDTO inviteList) {
        if (!hasPermission(inviteList.getEntityId(), MemberTypeEnum.valueOf(inviteList.getType().name()),false)) {
            throw new CustomerException("has no permission,to insert member must be owner");
        }
        Organization org = null;
        Team team = null;
        if(inviteList.getType()==InviteBelongEnum.ORGANIZATION){
            org=this.organizationDAO.selectByPrimaryKey(inviteList.getEntityId());
        }else{
            team=this.teamDAO.selectByPrimaryKey(inviteList.getEntityId());
        }
        if (!CollectionUtils.isEmpty(inviteList.getEmails())) {
            for (String email :new HashSet<>(inviteList.getEmails())) {
                UserInfoDTO invitedUser=null;
                try{
                    invitedUser= userService.getUserInfoByEmail(email);
                }catch (Exception e){
                    log.error(ExceptionUtil.stackTraceToString(e));
                }
                InsertMemberNeedInviteDTO dto=null;
                if(inviteList.getType()==InviteBelongEnum.ORGANIZATION){
                    dto=new InsertMemberNeedInviteDTO(org.getId(),null,email,MemberTypeEnum.ORGANIZATION);
                }else{
                    //判断member是否已经存在于组织中，如果已经存在则不再需要邮件确认
                    if(invitedUser!=null&&invitedUser.getId()!=null){
                        Member member = this.basicMemberManager.checkMemberExist(team.getOrganizationId(), invitedUser.getId(), MemberTypeEnum.ORGANIZATION);
                        if (member!=null){
                            InsertMemberDTO dtoNoNeedInvited=new InsertMemberDTO(team.getOrganizationId(),team.getId(),invitedUser.getId(),invitedUser.getName(),MemberTypeEnum.TEAM,null);
                            insertMember(dtoNoNeedInvited,false,false);
                            continue;
                        }
                    }
                    dto=new InsertMemberNeedInviteDTO(team.getOrganizationId(),team.getId(),email,MemberTypeEnum.TEAM);
                }
                insertMember(dto,false,false);
            }
        }
        //team的可以通过选择org列表中的id来进行添加
        if(!CollectionUtils.isEmpty(inviteList.getUserIds())&&inviteList.getType()==InviteBelongEnum.TEAM){
            for (Long id:inviteList.getUserIds()) {
                UserInfoDTO invitedUser=null;
                try{
                    invitedUser= userService.getUserInfoById(id);
                }catch (Exception e){
                    log.error(ExceptionUtil.stackTraceToString(e));
                }
                InsertMemberDTO dtoNoNeedInvited=new InsertMemberDTO(team.getOrganizationId(),team.getId(),invitedUser.getId(),invitedUser.getName(),MemberTypeEnum.TEAM,null);
                insertMember(dtoNoNeedInvited,false,false);
            }
        }
    }

    @Override
    public void acceptInvitationAndupdateMember(String invitedCipher) {
        Long   invitedId = null;
        try {
            invitedId = Long.valueOf(AESUtil.decryptInvitedInfo(URLDecoder.decode(invitedCipher,"UTF-8")));
        } catch (Exception e) {
            throw new CustomerException("parameter error");
        }
        this.inviteService.acceptInvited(invitedId);
    }

    private Long insertMember(InsertMemberDTO insertMemberDTO, Boolean isOwner, MemberStatusEnum status,Boolean ignorePermission) {
        if ((insertMemberDTO.getType() == MemberTypeEnum.ORGANIZATION && insertMemberDTO.getOrganizationId() == null) ||
                (insertMemberDTO.getType() == MemberTypeEnum.TEAM && insertMemberDTO.getTeamId() == null)) {

            throw new CustomerException("insert member parameter error");
        }
        Long entityId = insertMemberDTO.getType() == MemberTypeEnum.ORGANIZATION ? insertMemberDTO.getOrganizationId() : insertMemberDTO.getTeamId();
        if (!hasPermission(entityId, insertMemberDTO.getType(),ignorePermission)) {
            throw new CustomerException("has no permission,to insert member must be owner");
        }
        if (isOwner) {
            return this.basicMemberManager.insertMemberAsOwner(insertMemberDTO, status);
        }
        return this.basicMemberManager.insertMember(insertMemberDTO, status);

    }

    @Override
    public long insertMember(InsertMemberDTO insertMemberDTO, Boolean isOwner,Boolean ignorePermission) {
        return insertMember(insertMemberDTO, isOwner, MemberStatusEnum.NORMAL, ignorePermission);
    }

    @Override
    @Transactional
    public Long insertMember(InsertMemberNeedInviteDTO insertMemberDTO, Boolean isOwner,Boolean ignorePermission) {
        UserInfoDTO userInfo = userService.getUserInfoByEmail(insertMemberDTO.getEmail());
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        Long entityId=insertMemberDTO.getType()==MemberTypeEnum.ORGANIZATION?insertMemberDTO.getOrganizationId():insertMemberDTO.getTeamId();
        Member member = this.basicMemberManager.checkMemberExistIgnoreStatus(entityId, userInfo.getId(),insertMemberDTO.getEmail(),insertMemberDTO.getType());
        if(member!=null){
            return member.getId();
        }
        InsertInvitedDTO insertInvitedDTO = new InsertInvitedDTO(insertMemberDTO.getTeamId(), insertMemberDTO.getOrganizationId(),
                insertMemberDTO.getEmail(), InviteBelongEnum.valueOf(insertMemberDTO.getType().name()), currentUser.getId(), currentUser.getName());
        if (userInfo == null||userInfo.getId()==null) {
            Long invitedId=this.inviteService.insertAndInvitedRegister(insertInvitedDTO);
            InsertMemberDTO insertMember = new InsertMemberDTO(insertMemberDTO.getOrganizationId(), insertMemberDTO.getTeamId(), invitedId, insertMemberDTO.getEmail(), insertMemberDTO.getType(),invitedId);
            Long memberId= insertMember(insertMember, isOwner, MemberStatusEnum.WAITING_REGISTER, ignorePermission);
            addMemberInvitationJoinRecord(memberId,invitedId,MemberInvitationJoinType.EMAIL);
            return memberId;
        } else {
            Long invitedId=this.inviteService.insertAndInvitedJoin(insertInvitedDTO, userInfo);
            InsertMemberDTO insertMember = new InsertMemberDTO(insertMemberDTO.getOrganizationId(), insertMemberDTO.getTeamId(), userInfo.getId(), userInfo.getName(), insertMemberDTO.getType(),invitedId);
            Long memberId = insertMember(insertMember, isOwner, MemberStatusEnum.WAITING_ACCEPT, ignorePermission);
            addMemberInvitationJoinRecord(memberId,invitedId,MemberInvitationJoinType.EMAIL);
            return memberId;
        }
    }


    @Transactional
    @Override
    public boolean deleteMember(Long id) {
        Member member = this.memberDAO.selectByPrimaryKey(id);
        if (member == null) {
            throw new CustomerException("member is not exist");
        }
        Long entityId=member.getType()==MemberTypeEnum.ORGANIZATION?member.getOrganizationId():member.getTeamId();
        if (!hasPermission(entityId, member.getType(),false)) {
            throw new CustomerException("has no permission,to delete member must be owner");
        }
        int result = this.memberDAO.deleteByPrimaryKey(id);

        if (result > 0) {
            String logDesc = String.format("%s deleted member, id is: %s.", getCurrentUser().getId(), id);
            this.insertLog(id, null, BussinessLogOperateTypeEnum.DELETE, logDesc);
        }

        return result > 0;
    }

    @Override
    public void changeOrgMemberRoleDTO(ChangeOrgMemberRoleDTO changeOrgMemberRoleDTO) {
        Member member = this.memberDAO.findOrganizationMember(changeOrgMemberRoleDTO.getOrganizationId(), changeOrgMemberRoleDTO.getUserId());
        if (member == null) {
            throw new CustomerException("member not exist ");
        }
        if (!hasPermission(changeOrgMemberRoleDTO.getOrganizationId(), MemberTypeEnum.ORGANIZATION,false)) {
            throw new CustomerException("has no permission,to change role must be owner");
        }
        UserInfoDTO userinfo = userService.getUserInfoById(changeOrgMemberRoleDTO.getUserId());
        ChangeMemberRoleDTO dto = new ChangeMemberRoleDTO();
        dto.setEntityId(changeOrgMemberRoleDTO.getOrganizationId());
        dto.setUserId(changeOrgMemberRoleDTO.getUserId());
        dto.setUserName(userinfo.getName());
        dto.setType(MemberTypeEnum.ORGANIZATION);
        dto.setRole(changeOrgMemberRoleDTO.getRole());
        basicMemberManager.changeMemberRole(dto);

    }

    @Override
    public void changeTeamMemberRoleDTO(ChangeTeamMemberRoleDTO changeTeamMemberRoleDTO) {
        Member member = this.memberDAO.findTeamMember(changeTeamMemberRoleDTO.getTeamId(), changeTeamMemberRoleDTO.getUserId());
        if (member == null) {
            throw new CustomerException("member not exist ");
        }
        if (!hasPermission(changeTeamMemberRoleDTO.getTeamId(), MemberTypeEnum.TEAM,false)) {
            throw new CustomerException("has no permission,to change role must be owner");
        }
        UserInfoDTO userinfo = userService.getUserInfoById(changeTeamMemberRoleDTO.getUserId());
        ChangeMemberRoleDTO dto = new ChangeMemberRoleDTO();
        dto.setEntityId(changeTeamMemberRoleDTO.getTeamId());
        dto.setUserId(changeTeamMemberRoleDTO.getUserId());
        dto.setUserName(userinfo.getName());
        dto.setType(MemberTypeEnum.TEAM);
        dto.setRole(changeTeamMemberRoleDTO.getRole());
        basicMemberManager.changeMemberRole(dto);

    }

    @Override
    public List<MemberStatisticsDTO> listTeamMemberWithStatistics(Long teamId) {
        return this.memberDAO.listTeamMemberStatistics(teamId, null);
    }

    @Override
    public List<MemberStatisticsDTO> listOrganizationMemberWithStatistics(Long organizationId) {
        return this.memberDAO.listOrganizationMemberStatistics(organizationId, null);
    }

    @Override
    public List<MemberDTO> listOrganizationMembers(Long organizationId, Boolean onlyOwner) {
        return BeanConvertUtils.batchTransform(MemberDTO.class, this.memberDAO.listOrganizationMembers(organizationId, onlyOwner));
    }

    @Override
    public List<MemberDTO> listTeamMembers(Long teamId, Boolean onlyOwner) {
        return BeanConvertUtils.batchTransform(MemberDTO.class, this.memberDAO.listTeamMembers(teamId, onlyOwner));
    }

    @Override
    public List<SimpleUserInfoDTO> listOrganizationMemberConverToUser(Long organizationId) {
        return this.memberDAO.listUser(organizationId,MemberTypeEnum.ORGANIZATION.toString());
    }

    @Override
    public List<SimpleUserInfoDTO> listTeamMemberConverToUser(Long teamId) {
        return this.memberDAO.listUser(teamId,MemberTypeEnum.TEAM.toString());
    }

    @Override
    public Integer countOrganizationMember(Long id) {
        return this.memberDAO.countOrganizationMember(id);
    }

    @Override
    public PageInfo<MemberDTO> listOrganizationMembersByPage(Long organizationId, Integer pageNumber, Integer pageSize) {
        if (pageNumber == null || pageSize == null) {
            throw new CustomerException("pageSize or pageNumber must not be null");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Member> list = this.memberDAO.listOrganizationMembers(organizationId, false);
        PageInfo page = new PageInfo(list);
        page.setList(BeanConvertUtils.batchTransform(MemberDTO.class, list));
        return page;
    }

    @Override
    public PageInfo<MemberDTO> listTeamMembersByPage(Long teamId, Integer pageNumber, Integer pageSize) {
        if (pageNumber == null || pageSize == null) {
            throw new CustomerException("pageSize or pageNumber must not be null");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Member> list = this.memberDAO.listOrganizationMembers(teamId, false);
        PageInfo page = new PageInfo(list);
        page.setList(BeanConvertUtils.batchTransform(MemberDTO.class, list));
        return page;
    }

    @Override
    public PageInfo<MemberStatisticsDTO> listOrganizationMemberWithStatisticsByPage(Long organizationId, Integer pageNumber, Integer pageSize, String name) {
        if (pageNumber == null || pageSize == null) {
            throw new CustomerException("pageSize or pageNumber must not be null");
        }
        String nameQuery = StringUtils.isEmpty(name) ? null : name;
        PageHelper.startPage(pageNumber, pageSize);
        return new PageInfo<>(this.memberDAO.listOrganizationMemberStatistics(organizationId, nameQuery));
    }

    @Override
    public PageInfo<MemberStatisticsDTO> listTeamMemberWithStatisticsByPage(Long teamId, Integer pageNumber, Integer pageSize, String name) {
        if (pageNumber == null || pageSize == null) {
            throw new CustomerException("pageSize or pageNumber must not be null");
        }
        String nameQuery = StringUtils.isEmpty(name) ? null : name;
        PageHelper.startPage(pageNumber, pageSize);
        return new PageInfo<>(this.memberDAO.listTeamMemberStatistics(teamId, nameQuery));
    }

    @Override
    public List<MemberSimpleInfoDTO> listPartnersCorssOrganization(UserInfoDTO user) {
      return this.memberDAO.listAllMembersByUser(user.getId());
    }

    @Override
    public String generateOrganizationInvitationLink(Long organizationId) {
        Organization org = this.organizationDAO.selectByPrimaryKey(organizationId);
        if(org==null){
            throw new CustomerException("organization not exist! ");
        }
        if(!hasPermission(organizationId,MemberTypeEnum.ORGANIZATION,false)){
            throw new CustomerException("generate organization invitation link failer,should be a owner! ");
        }
        UserInfoDTO currentUser = getCurrentUser();
        InvitePublicLink inviteLink=new InvitePublicLink();
        inviteLink.setOrganizationId(org.getId());
        inviteLink.setInviterId(currentUser.getId());
        inviteLink.setEntityType(MemberTypeEnum.ORGANIZATION);
        //设置过期时间为1天
        inviteLink.setExpireTime(System.currentTimeMillis()+24*60*60*1000);
        inviteLink.initializeForInsert();
        publicLinkInviteDAO.insertSelective(inviteLink);
        String invicationMessage=JSONObject.toJSONString(inviteLink);
        //生成加密链接
        try {
            JSONObject result=new JSONObject();
            result.put("info",URLEncoder.encode(AESUtil.encryptInvitedInfo(String.valueOf(inviteLink.getId())), "UTF-8"));
            return result.toJSONString();
        } catch (UnsupportedEncodingException e) {
            throw new CustomerException("generate organization invitation link failer");
        }
    }

    @Override
    public String generateTeamInvitationLink(Long teamId) {
        Team team = this.teamDAO.selectByPrimaryKey(teamId);
        if(team==null){
            throw new CustomerException("team not exist! ");
        }
        if(!hasPermission(team.getId(),MemberTypeEnum.TEAM,false)){
            throw new CustomerException("generate team invitation link failer,should be a owner! ");
        }
        UserInfoDTO currentUser = getCurrentUser();
        InvitePublicLink inviteLink=new InvitePublicLink();
        inviteLink.setOrganizationId(team.getOrganizationId());
        inviteLink.setTeamId(team.getId());
        inviteLink.setInviterId(currentUser.getId());
        inviteLink.setEntityType(MemberTypeEnum.TEAM);
        //设置过期时间为1天
        inviteLink.setExpireTime(System.currentTimeMillis()+24*60*60*1000);
        inviteLink.initializeForInsert();
        publicLinkInviteDAO.insertSelective(inviteLink);
        //生成加密链接
        try {
            JSONObject result=new JSONObject();
            result.put("info",URLEncoder.encode(AESUtil.encryptInvitedInfo(String.valueOf(inviteLink.getId())), "UTF-8"));
            return result.toJSONString();
        } catch (UnsupportedEncodingException e) {
            throw new CustomerException("generate organization invitation link failer");
        }
    }

    @Override
    @Transactional
    public String joinTOGroupByPublicINvitationLink(String info) {
        Long  invication = null;
        try {
            invication = Long.valueOf(AESUtil.decryptInvitedInfo(URLDecoder.decode(info,"UTF-8")));
        } catch (Exception e) {
            throw new CustomerException(new IResultEnumResult(){

                @Override
                public String getCode() {
                    return "M0005";
                }

                @Override
                public String getMessage() {
                    return "Join Team failer,paramter illegal";
                }
            });
        }
        InvitePublicLink inviteLink=this.publicLinkInviteDAO.selectByPrimaryKey(invication);
        checkPulbicLinkInvite(inviteLink);
        UserInfoDTO currentUser = getCurrentUser();
        InsertMemberDTO insertMemberDTO=new InsertMemberDTO(inviteLink.getOrganizationId(),inviteLink.getTeamId(),currentUser.getId(),currentUser.getName(),inviteLink.getEntityType(),null);
        Long memberId=insertMember(insertMemberDTO,false,true);
        addMemberInvitationJoinRecord(memberId,inviteLink.getId(),MemberInvitationJoinType.PUBLIC_LINK);
        //如果team邀请，同时将user添加为org的成员
        if (inviteLink.getEntityType() == MemberTypeEnum.TEAM) {
            Member orgMember = basicMemberManager.checkMemberExistIgnoreStatus(inviteLink.getOrganizationId(), currentUser.getId(), null, MemberTypeEnum.ORGANIZATION);
            if(orgMember!=null){
                if(orgMember.getStatus()!=MemberStatusEnum.NORMAL){
                    orgMember.setStatus(MemberStatusEnum.NORMAL);
                    orgMember.initializeForUpdate();
                    memberDAO.updateByPrimaryKeySelective(orgMember);
                }
            }else{
                InsertMemberDTO insetDTO=new InsertMemberDTO();
                insetDTO.setOrganizationId(inviteLink.getOrganizationId());
                insetDTO.setUserName(currentUser.getName());
                insetDTO.setType(MemberTypeEnum.ORGANIZATION);
                insetDTO.setUserId(currentUser.getId());
                Long orgMemberId= basicMemberManager.insertMember(insetDTO,MemberStatusEnum.NORMAL);
                addMemberInvitationJoinRecord(orgMemberId,inviteLink.getId(),MemberInvitationJoinType.PUBLIC_LINK);
            }
        }
        return inviteLink.getEntityType() == MemberTypeEnum.TEAM?this.teamDAO.selectByPrimaryKey(inviteLink.getTeamId()).getName():this.organizationDAO.selectByPrimaryKey(inviteLink.getOrganizationId()).getName();
    }

    @Override
    public InvitationInfoDTO decryptInvitationInfo(String info) {
        Long  invication = null;
        try {
            invication = Long.valueOf(AESUtil.decryptInvitedInfo(URLDecoder.decode(info,"UTF-8")));
        } catch (Exception e) {
            throw new CustomerException(new IResultEnumResult(){

                @Override
                public String getCode() {
                    return "M0006";
                }

                @Override
                public String getMessage() {
                    return "decrypt Invotation info  failer, paramter illegal";
                }
            });
        }
        InvitePublicLink inviteLink=this.publicLinkInviteDAO.selectByPrimaryKey(invication);
        InvitationInfoDTO invitationInfoDTO=new InvitationInfoDTO();
        if(inviteLink==null){
            return invitationInfoDTO;
        }
        UserInfoDTO currentUser = getCurrentUser();
        UserInfoDTO inviter=userService.getUserInfoById(inviteLink.getInviterId());
        Organization organization=this.organizationDAO.selectByPrimaryKey(inviteLink.getOrganizationId());
        invitationInfoDTO.setOrganizationId(organization.getId());
        invitationInfoDTO.setOrganizationName(organization.getName());
        invitationInfoDTO.setInviterId(inviter.getId());
        invitationInfoDTO.setInviterName(inviter.getName());
        if(inviteLink.getTeamId()!=null){
            Team team=this.teamDAO.selectByPrimaryKey(inviteLink.getTeamId());
            invitationInfoDTO.setTeamId(team.getId());
            invitationInfoDTO.setTeamName(team.getName());
            if(this.memberDAO.findTeamMember(team.getId(),currentUser.getId())!=null){
                invitationInfoDTO.setExistence(true);
            }else{
                invitationInfoDTO.setExistence(false);
            }
        }else{
            if(this.memberDAO.findOrganizationMember(organization.getId(),currentUser.getId())!=null){
                invitationInfoDTO.setExistence(true);
            }else{
                invitationInfoDTO.setExistence(false);
            }
        }
        return invitationInfoDTO;
    }

    @Override
    public List<SimpleUserInfoDTO> listUsualWeeklyPlanReportReciver(Long organizationId, Long id) {
        Set<SimpleUserInfoDTO> reciver=new HashSet<>();
        //获取配置需要接受的人
        List<Long> settingRecivers=memberSettingService.listUsualWeeklyPlanReportReciver(organizationId,id);
        if(!CollectionUtils.isEmpty(settingRecivers)){
            settingRecivers.forEach( userId->{
                try{
                    UserInfoDTO userInfo = userService.getUserInfoById(userId);
                    SimpleUserInfoDTO simpleUserInfoDTO=new SimpleUserInfoDTO();
                    simpleUserInfoDTO.setId(userInfo.getId());
                    simpleUserInfoDTO.setUserName(userInfo.getName());
                    reciver.add(simpleUserInfoDTO);

                }catch (Exception e){
                    log.error(ExceptionUtil.stackTraceToString(e));
                }
            });
            List<SimpleUserInfoDTO> result=new ArrayList<>();
            result.addAll(reciver);
            return result;
        }
        //如果没有对应的配置，则按照team和org的人
        Organization org = this.organizationDAO.selectByPrimaryKey(organizationId);
        SimpleUserInfoDTO orgManager=new SimpleUserInfoDTO();
        orgManager.setId(org.getManagerId());
        orgManager.setUserName(org.getManagerName());
        //当前用户是组织的manager，发送给自己
        if(id.equals(orgManager.getId())){
            reciver.add(orgManager);
        }
        // list用户所在team，如果是team leader 则发送给org manager 否则发送给 team leader
        List<Team> teams = this.teamDAO.listTeamsByUserId(organizationId,id);
        boolean userIsTeamLeader=false;
        if(!CollectionUtils.isEmpty(teams)){
            for (Team team:teams) {
                //自己本身是一个team leader，则发送给org manager
                if(id.equals(team.getLeaderId())){
                    reciver.add(orgManager);
                    userIsTeamLeader=true;
                }
            }
        }
        if(!CollectionUtils.isEmpty(teams)){
            for (Team team:teams) {
                //自己不是一个team leader
                if(!userIsTeamLeader){
                    SimpleUserInfoDTO teamLeader=new SimpleUserInfoDTO();
                    teamLeader.setId(team.getLeaderId());
                    teamLeader.setUserName(team.getLeaderName());
                    reciver.add(teamLeader);
                }
            }
        }
        //不属于组织任何的团队，则汇报给org manager
        if(reciver.size()==0){
            reciver.add(orgManager);
        }
        List<SimpleUserInfoDTO> result=new ArrayList<>();
        result.addAll(reciver);
        return result;
    }

    @Override
    public Map<String, List<Long>> listOrganizationTeamMembers(Long id) {
        List<Member> teamMembers=this.memberDAO.findOrgAllTeamMembers(id);
        List<TeamStatisticsDTO> teams = this.teamDAO.listTeamsStatisticsByOrganization(id);
        Map<Long,String> teamNameMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(teams)){
            for (TeamStatisticsDTO team:teams) {
                teamNameMap.put(team.getId(),team.getName());
            }
        }
        Map<String, List<Long>> result=new HashMap<>();
        if(!CollectionUtils.isEmpty(teamNameMap)&&!CollectionUtils.isEmpty(teamMembers)){
            for (Member member:teamMembers) {
                String teamName=teamNameMap.get(member.getTeamId());
                List<Long> list = result.get(teamName);
                if(list==null){
                    list=new ArrayList<>();
                    result.put(teamName,list);
                }
                list.add(member.getUserId());

            }
        }

        return result;
    }


    public void addMemberInvitationJoinRecord(Long memberId,Long invicationId,MemberInvitationJoinType type){
        MemberInvitationJoinRecord invitationJoinRecord=new MemberInvitationJoinRecord();
        invitationJoinRecord.setInvitationId(invicationId);
        invitationJoinRecord.setMemberId(memberId);
        invitationJoinRecord.setInviationType(type);
        invitationJoinRecord.initializeForInsert();
        memberInvitationJoinRecordDAO.insertSelective(invitationJoinRecord);
    }

    private Boolean hasPermission(Long id, MemberTypeEnum type,Boolean ignore) {
        if (ignore) {
            return true;
        }
        if (type == MemberTypeEnum.ORGANIZATION) {
            return organizationService.hasPermission(id,PrivilegeTypeEnum.UPDATABLE);
        } else {
            return teamService.hasPermission(id,PrivilegeTypeEnum.UPDATABLE);
        }
    }
    
    private void checkPulbicLinkInvite(InvitePublicLink inviteLink){
        if(inviteLink==null){
            throw new CustomerException(new IResultEnumResult(){

                @Override
                public String getCode() {
                    return "M0005";
                }

                @Override
                public String getMessage() {
                    return "the invitation link not exist!";
                }
            });
        }
        Long now=System.currentTimeMillis();
        if(now>inviteLink.getExpireTime()){
            throw new CustomerException(new IResultEnumResult(){

                @Override
                public String getCode() {
                    return "M0005";
                }

                @Override
                public String getMessage() {
                    return "the invitation link expired!!";
                }
            });
        }
    }

    private void insertLog(Long memberId, Long organizationId, BussinessLogOperateTypeEnum operateTypeEnum, String desc) {
        this.insertLog(memberId, organizationId, BussinessLogEntityEnum.MEMBER, operateTypeEnum, desc);
    }

    @Autowired
    private MemberDAO memberDAO;


    @Autowired
    private BasicMemberManager basicMemberManager;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private InvitePublicLinkDAO publicLinkInviteDAO;

    @Autowired
    public MemberInvitationJoinRecordDAO memberInvitationJoinRecordDAO;

    @Autowired
    public MemberSettingService memberSettingService;

}
