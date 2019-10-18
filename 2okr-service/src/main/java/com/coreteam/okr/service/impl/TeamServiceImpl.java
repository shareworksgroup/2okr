package com.coreteam.okr.service.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.Authorization.OrganizationPermissionRulesParser;
import com.coreteam.okr.Authorization.TeamPermissionRulesParser;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.constant.*;
import com.coreteam.okr.dao.TeamDAO;
import com.coreteam.okr.dto.Authorization.Privilege;
import com.coreteam.okr.dto.Authorization.TeamResourcesDescDTO;
import com.coreteam.okr.dto.member.ChangeTeamMemberRoleDTO;
import com.coreteam.okr.dto.member.InsertMemberDTO;
import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.objective.CountObjectiveByStatusDTO;
import com.coreteam.okr.dto.objective.ListObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveStatisticsDTO;
import com.coreteam.okr.dto.team.*;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Team;
import com.coreteam.okr.manager.BasicMemberManager;
import com.coreteam.okr.service.*;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @ClassName: TeamServiceImpl
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 11:18
 * @Version 1.0.0
 */
@Service
@Slf4j
public class TeamServiceImpl extends BaseService implements TeamService {
    @Autowired
    private OrganizationPermissionRulesParser organizationPermissionRulesParser;
    @Autowired
    private TeamPermissionRulesParser teamPermissionRulesParser;

    @Transactional
    @Override
    public Long insertTeam(InsertTeamDTO insertTeamDTO) {
        if (!hasAddTeamPermission(insertTeamDTO.getOrganizationId())) {
            throw new CustomerException("has not permission to add the team,should be organization owner");
        }
        Team team = new Team();
        BeanUtils.copyProperties(insertTeamDTO, team);
        UserInfoDTO currentUser = getCurrentUser();
        team.setCreatedName(currentUser.getName());
        team.setCreatedUser(currentUser.getId());
        team.initializeForInsert();
        this.dao.insert(team);
        InsertMemberDTO ownerDTO = new InsertMemberDTO(insertTeamDTO.getOrganizationId(), team.getId(), currentUser.getId(), currentUser.getName(), MemberTypeEnum.TEAM, null);
        this.memberService.insertMember(ownerDTO, true, true);
        //将leader设置为owner
        UserInfoDTO leader = null;
        if (!currentUser.getId().equals(insertTeamDTO.getLeaderId())) {
            leader = userService.getUserInfoById(insertTeamDTO.getLeaderId());
            InsertMemberDTO leaderDTO = new InsertMemberDTO(insertTeamDTO.getOrganizationId(), team.getId(), leader.getId(), leader.getName(), MemberTypeEnum.TEAM, null);
            this.memberService.insertMember(leaderDTO, true, true);
        }
        //添加邀请的人
        if (insertTeamDTO.getMembers() != null) {
            for (Long memberId : new HashSet<>(insertTeamDTO.getMembers())) {
                if (!memberId.equals(currentUser.getId()) && !memberId.equals(insertTeamDTO.getLeaderId())) {
                    try {
                        UserInfoDTO invitedUser = userService.getUserInfoById(memberId);
                        InsertMemberDTO memberDTO = new InsertMemberDTO(insertTeamDTO.getOrganizationId(), team.getId(), invitedUser.getId(), invitedUser.getName(), MemberTypeEnum.TEAM, null);
                        this.memberService.insertMember(memberDTO, false, true);
                    } catch (Exception e) {
                        log.error(ExceptionUtil.stackTraceToString(e));

                    }
                }

            }
        }
        String logDesc = String.format("%s inserted team %s.", currentUser.getName(), team.getName());
        this.insertLog(team.getId(), team.getOrganizationId(), BussinessLogOperateTypeEnum.UPDATE, logDesc);
        return team.getId();
    }

    @Override
    public boolean updateTeam(Long id, UpdateTeamDTO updateTeamDTO) {
        if (!hasPermission(id, PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException("has not permission to operate the team");
        }
        if (updateTeamDTO.getLeaderId() != null) {
            Team old = this.dao.selectByPrimaryKey(id);
            changeTeamLeader(old, updateTeamDTO);
        }
        Team team = new Team();
        team.setId(id);
        BeanUtils.copyProperties(updateTeamDTO, team);
        team.initializeForUpdate();
        int result = this.dao.updateByPrimaryKeySelective(team);
        if (result > 0) {
            String logDesc = String.format("%s updated team %s.", getCurrentUser().getName(), team.getName());
            this.insertLog(team.getId(), team.getOrganizationId(), BussinessLogOperateTypeEnum.UPDATE, logDesc);
        }
        return result > 0;
    }

    @Override
    public boolean deleteTeam(Long id) {
        if (!hasPermission(id, PrivilegeTypeEnum.DELETABLE)) {
            throw new CustomerException("has not permission to operate the team");
        }
        int result = this.dao.deleteByPrimaryKey(id);
        if (result > 0) {
            String logDesc = String.format("%s deleted team, id is: %s.", getCurrentUser(), id);
            this.insertLog(id, null, BussinessLogOperateTypeEnum.DELETE, logDesc);
        }
        return result > 0;
    }

    @Override
    public TeamDTO getTeam(Long id) {
        Team team = this.dao.selectByPrimaryKey(id);
        TeamDTO dto = new TeamDTO();
        if (team != null) {
            BeanUtils.copyProperties(team, dto);
            combinePrivilege(dto);

        }
        return dto;
    }

    @Override
    public List<TeamStatisticsDTO> listTeamsWithStatisticsByOrganizationFilter(Long organizationId) {
        List<TeamStatisticsDTO> list = this.dao.listTeamsStatisticsByOrganization(organizationId);
        combinePrivilege(list, organizationId);
        return list;
    }

    @Override
    public List<TeamStatisticsDTO> listTeamsWithStatisticsInvitableByOrganizationFilter(Long organizationId) {
        List<TeamStatisticsDTO> teamList = listTeamsWithStatisticsByOrganizationFilter(organizationId);
        UserInfoDTO currentUser = getCurrentUser();
        if(CollectionUtils.isEmpty(teamList)||organizationService.hasPermission(organizationId,PrivilegeTypeEnum.TEAM_ADDABLE)){
            return teamList;
        }

        //过滤掉不具备权限的
        List<TeamStatisticsDTO> teamListAsOwner=new ArrayList<>();
        teamList.forEach(teamStatisticsDTO -> {
            if(teamStatisticsDTO.getPrivilege().getUpdatable()){
                teamListAsOwner.add(teamStatisticsDTO);
            }
        });
        return teamListAsOwner;
    }

    @Override
    public List<TeamStatisticsWithMembersDTO> listTeamsWithStatisticsAndMembersByOrganizationFilter(Long organizationId) {
        List<TeamStatisticsDTO> teamList = listTeamsWithStatisticsByOrganizationFilter(organizationId);
        List<TeamStatisticsWithMembersDTO> teamWithMemberList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(teamList)){
            teamWithMemberList= BeanConvertUtils.batchTransform(TeamStatisticsWithMembersDTO.class,teamList);
            teamWithMemberList.forEach(team->{
                team.setMembers(memberService.listTeamMembers(team.getId(),false));
            });
        }
        return teamWithMemberList;
    }


    @Override
    public PageInfo<ObjectiveDTO> listTeamLevelObjectives(Long tearmId, Integer pageNumber, Integer pageSize, Date start, Date end) {
        TeamDTO team = this.getTeam(tearmId);
        ListObjectiveDTO queryDTO = new ListObjectiveDTO();
        queryDTO.setBusinessId(tearmId);
        queryDTO.setPageNumber(pageNumber);
        queryDTO.setPageSize(pageSize);
        queryDTO.setStart(start);
        queryDTO.setEnd(end);
        queryDTO.setLevel(ObjectiveLevelEnum.TEAM.name());
        queryDTO.setOrganizationId(team.getOrganizationId());
        return okrService.listObjectiveByLevelFilter(queryDTO);
    }

    @Override
    public ObjectiveStatisticsDTO getTeamLevelObjectiveStatistics(Long teamId, Date start, Date end) {
        TeamDTO team = this.getTeam(teamId);
        CountObjectiveByStatusDTO query = new CountObjectiveByStatusDTO();
        query.setBusinessId(teamId);
        query.setStart(start);
        query.setEnd(end);
        query.setLevel(ObjectiveLevelEnum.TEAM.name());
        query.setOrganizationId(team.getOrganizationId());
        ObjectiveStatisticsDTO dto = okrService.countObjectiveNumByStatusAndListOverTimeDataByLevelFilter(query);
        if (dto == null) {
            dto = new ObjectiveStatisticsDTO();
        }
        return dto;
    }

    @Override
    public Boolean hasPermission(Long teamId, PrivilegeTypeEnum type) {
        UserInfoDTO currentUser = getCurrentUser();
        Team team = this.dao.selectByPrimaryKey(teamId);
        List<MemberDTO> teamOwners = this.memberService.listTeamMembers(teamId, true);
        List<MemberDTO> orgOwners = this.memberService.listOrganizationMembers(team.getOrganizationId(), true);
        TeamResourcesDescDTO descDTO = new TeamResourcesDescDTO();
        descDTO.setOrgOwner(orgOwners);
        descDTO.setTeamOwner(teamOwners);
        Privilege privilege = teamPermissionRulesParser.getUserPrivilege(currentUser, descDTO);
        switch (type) {
            case UPDATABLE:
                return privilege.getUpdatable();
            case DELETABLE:
                return privilege.getDeletable();
        }

        return false;
    }

    public Boolean hasAddTeamPermission(Long orgId) {
        UserInfoDTO currentUser = getCurrentUser();
        return organizationPermissionRulesParser.getUserPrivilege(currentUser, this.memberService.listOrganizationMembers(orgId, true)).getTeamAddable();
    }

    private void combinePrivilege(TeamDTO teamDTO) {
        UserInfoDTO currentUser = getCurrentUser();
        List<MemberDTO> teamOwners = this.memberService.listTeamMembers(teamDTO.getId(), true);
        List<MemberDTO> orgOwners = this.memberService.listOrganizationMembers(teamDTO.getOrganizationId(), true);
        TeamResourcesDescDTO descDTO = new TeamResourcesDescDTO();
        descDTO.setOrgOwner(orgOwners);
        descDTO.setTeamOwner(teamOwners);
        teamDTO.setPrivilege(teamPermissionRulesParser.getUserPrivilege(currentUser, descDTO));
    }

    private void combinePrivilege(List<TeamStatisticsDTO> teamList, Long orgId) {
        if (!CollectionUtils.isEmpty(teamList)) {
            UserInfoDTO currentUser = getCurrentUser();
            List<MemberDTO> orgOwners = this.memberService.listOrganizationMembers(orgId, true);
            teamList.forEach(team -> {
                List<MemberDTO> teamOwners = this.memberService.listTeamMembers(team.getId(), true);
                TeamResourcesDescDTO descDTO = new TeamResourcesDescDTO();
                descDTO.setOrgOwner(orgOwners);
                descDTO.setTeamOwner(teamOwners);
                team.setPrivilege(teamPermissionRulesParser.getUserPrivilege(currentUser, descDTO));
            });

        }
    }

    private void changeTeamLeader(Team old, UpdateTeamDTO newTeamInfo) {
        if (newTeamInfo.getLeaderId().equals(old.getLeaderId())) {
            return;
        }
        //将新的leader设置为owner
        ChangeTeamMemberRoleDTO newLeaderDTO = new ChangeTeamMemberRoleDTO();
        newLeaderDTO.setTeamId(old.getId());
        newLeaderDTO.setRole(MemberRoleEnum.OWNER);
        newLeaderDTO.setUserId(newTeamInfo.getLeaderId());
        this.memberService.changeTeamMemberRoleDTO(newLeaderDTO);

        //将原先的leader设置为普通的member
        if (old.getLeaderId() != null) {
            ChangeTeamMemberRoleDTO oldLeaderDTO = new ChangeTeamMemberRoleDTO();
            oldLeaderDTO.setTeamId(old.getId());
            oldLeaderDTO.setRole(MemberRoleEnum.MEMBER);
            oldLeaderDTO.setUserId(old.getLeaderId());
            this.memberService.changeTeamMemberRoleDTO(oldLeaderDTO);
        }

    }

    private void insertLog(Long entityId, Long organizationId, BussinessLogOperateTypeEnum operateTypeEnum, String desc) {
        this.insertLog(entityId, organizationId, BussinessLogEntityEnum.TEAM, operateTypeEnum, desc);
    }

    @Autowired
    private TeamDAO dao;

    @Autowired
    private BasicMemberManager basicMemberManager;

    @Autowired
    private OkrService okrService;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrganizationService organizationService;

}
