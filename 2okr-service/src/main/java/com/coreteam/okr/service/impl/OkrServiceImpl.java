package com.coreteam.okr.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.Authorization.KeyResultPermissionRulesParser;
import com.coreteam.okr.Authorization.ObjectivePermissionRulesParser;
import com.coreteam.okr.common.util.DateUtil;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.constant.*;
import com.coreteam.okr.dao.*;
import com.coreteam.okr.dto.Authorization.ObjectivePrivilege;
import com.coreteam.okr.dto.Authorization.ObjectiveResourcesDesc;
import com.coreteam.okr.dto.Notify.ObjectiveOffTraceSystemNotifyDTO;
import com.coreteam.okr.dto.Notify.ObjectiveUpdateSystemNotifyDTO;
import com.coreteam.okr.dto.log.BussinessLogDTO;
import com.coreteam.okr.dto.log.GetPagedBussinessLogDTO;
import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.dto.plan.ListWeeklyPlanDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanSingleObjectiveDTO;
import com.coreteam.okr.dto.team.TeamStatisticsDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.*;
import com.coreteam.okr.manager.Notify;
import com.coreteam.okr.manager.NotifyManager;
import com.coreteam.okr.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: OkrServiceImpl
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/19 9:44
 * @Version 1.0.0
 */
@Service
@Slf4j
public class OkrServiceImpl extends BaseService implements OkrService {

    @Autowired
    private ObjectivePermissionRulesParser objectivePermissionRulesParser;
    @Autowired
    private KeyResultPermissionRulesParser keyResultPermissionRulesParser;

    private static final Double EXCEEDED_RATE = 1.00;
    private static final Double ON_TRACK_RATE = 0.66;
    private static final Double AT_TRACK_RATE = 0.33;
    DecimalFormat df = new DecimalFormat("0.00");
    DecimalFormat df2 = new DecimalFormat("0");


    /*=========================================Objective base crud ===================================================*/

    @Transactional
    @Override
    public long insertObjective(InsertObjectiveDTO createObjectiveRequestDTO) {
        if (createObjectiveRequestDTO.getPeriodEndTime().before(createObjectiveRequestDTO.getPeriodStartTime())) {
            throw new CustomerException("the start time must before end time");
        }
        if (createObjectiveRequestDTO.getOrganizationId() == null) {
            throw new CustomerException("organization id must not be null");
        }
        if (createObjectiveRequestDTO.getLevel() == ObjectiveLevelEnum.TEAM && createObjectiveRequestDTO.getTeamId() == null) {
            throw new CustomerException("team id must not be null");
        }
        if (createObjectiveRequestDTO.getLevel() != ObjectiveLevelEnum.TEAM) {
            createObjectiveRequestDTO.setTeamId(null);
        }
        UserInfoDTO owner = userService.getUserInfoById(createObjectiveRequestDTO.getOwnerId());
        UserInfoDTO validator = userService.getUserInfoById(createObjectiveRequestDTO.getValidatorId());
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        Objective objective = new Objective();
        BeanConvertUtils.copyEntityProperties(createObjectiveRequestDTO, objective);
        objective.setStatus(ObjectiveStatusEnum.OPEN);
        objective.setBusinessStatus(ObjectiveBusinessStatusEnum.ON_TRACK);
        if (!StringUtils.isEmpty(createObjectiveRequestDTO.getAlignment())) {
            objective.setLinkedObjectiveId(Long.valueOf(createObjectiveRequestDTO.getAlignment()));
        }
        objective.setOwnerName(owner.getName());
        objective.setValidatorName(validator.getName());
        objective.setCreatedName(currentUser.getName());
        objective.setCreatedUser(currentUser.getId());
        objective.setProgress(0.0);
        objective.initializeForInsert();
        this.objectiveDAO.insert(objective);
        String desc = String.format("%s created Objective %s.", getCurrentUser().getName(), objective.getName());
        this.insertLog(objective.getId(), objective.getOrganizationId(), BussinessLogOperateTypeEnum.INSERT, desc);
        return objective.getId();
    }


    @Override
    @Transactional
    public boolean updateObjective(UpdateObjectiveDTO updateObjectiveDTO) {
        //校验权限
        if (!hasPermission(updateObjectiveDTO.getId(), PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException("has not permission,to update objective must be owner");
        }
        Objective objective = this.objectiveDAO.selectByPrimaryKey(updateObjectiveDTO.getId());
        if (objective == null) {
            throw new CustomerException("object not exist ,id:" + updateObjectiveDTO.getId());
        }
        //校验objective
        if (objective.getStatus() == ObjectiveStatusEnum.CLOSED) {
            throw new CustomerException("the Objective is closed");
        }
        //检查level
        if (updateObjectiveDTO.getLevel() == null) {
            updateObjectiveDTO.setTeamId(null);
        } else {
            if (updateObjectiveDTO.getLevel() == ObjectiveLevelEnum.TEAM && updateObjectiveDTO.getTeamId() == null) {
                throw new CustomerException("team id must not be null");
            }
        }

        //校验owner 和validator
        if (updateObjectiveDTO.getOwnerId() != null) {
            UserInfoDTO owner = userService.getUserInfoById(updateObjectiveDTO.getOwnerId());
            updateObjectiveDTO.setOwnerName(owner.getName());
        } else {
            updateObjectiveDTO.setOwnerName(null);
        }
        if (updateObjectiveDTO.getValidatorId() != null) {
            UserInfoDTO validator = userService.getUserInfoById(updateObjectiveDTO.getValidatorId());
            updateObjectiveDTO.setValidatorName(validator.getName());
        } else {
            updateObjectiveDTO.setValidatorName(null);
        }

        BeanUtils.copyProperties(updateObjectiveDTO, objective);
        if (!StringUtils.isEmpty(updateObjectiveDTO.getAlignment())) {
            objective.setLinkedObjectiveId(Long.valueOf(updateObjectiveDTO.getAlignment()));
        } else {
            objective.setLinkedObjectiveId(null);
        }
        objective.initializeForUpdate();
        int result = this.objectiveDAO.updateByPrimaryKeySelective(objective);
        if (result > 0) {
            UserInfoDTO currentUser = getCurrentUser();
            String logDesc = String.format("%s updated objective %s.", currentUser.getName(), objective.getName());
            this.insertLog(objective.getId(), objective.getOrganizationId(), BussinessLogOperateTypeEnum.UPDATE, logDesc);
        }
        return result > 0;
    }

    @Override
    @Transactional
    public void updateObjectiveStatus(UpdateObjectiveStatusDTO updateObjectiveDTO) {
        if (updateObjectiveDTO.getPeriodEndTime().before(updateObjectiveDTO.getPeriodStartTime())) {
            throw new CustomerException("the start time must before end time");
        }
        Objective object = this.objectiveDAO.selectByPrimaryKey(updateObjectiveDTO.getId());
        if (object == null) {
            throw new CustomerException("object not exist ,id:" + updateObjectiveDTO.getId());
        }
        if (object.getStatus() == ObjectiveStatusEnum.CLOSED) {
            throw new CustomerException("the Objective is closed");
        }
        if (!hasPermission(updateObjectiveDTO.getId(), PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException("has not permission,to update objective must be owner");
        }
        object.setPeriodStartTime(updateObjectiveDTO.getPeriodStartTime());
        object.setPeriodEndTime(updateObjectiveDTO.getPeriodEndTime());
        calObjectiveProgress(object);
        Date lastUpdate = object.getUpdatedAt();
        object.initializeForUpdate();
        int result = this.objectiveDAO.updateByPrimaryKeySelective(object);
        //Objective的更新添加防抖缄默，避免同一Objective的大量的更新通知
        if(lastUpdate!=null&&(System.currentTimeMillis()-lastUpdate.getTime()>1000*60*30)){
            sendObjectiveUpdateNotify(object);
        }
        if (result > 0) {
            UserInfoDTO currentUser = getCurrentUser();
            String logDesc = String.format("%s updated objective %s.", currentUser.getName(), object.getName());
            this.insertLog(object.getId(), object.getOrganizationId(), BussinessLogOperateTypeEnum.UPDATE, logDesc);
        }
    }


    @Override
    public void updateObjectiveProgress(ObjectiveDTO objectiveDTO) {
        Objective objective = this.objectiveDAO.selectByPrimaryKey(objectiveDTO.getId());
        BeanConvertUtils.copyEntityProperties(objectiveDTO, objective);
        calObjectiveProgress(objective);
        Date lastUpdate = objective.getUpdatedAt();
        if(lastUpdate!=null&&(System.currentTimeMillis()-lastUpdate.getTime()>1000*60*30)){
            sendObjectiveUpdateNotify(objective);
        }
        objective.initializeForUpdate();
        this.objectiveDAO.updateByPrimaryKeySelective(objective);
    }

    @Transactional
    @Override
    public boolean deleteObjective(Long id) {
        if (!hasPermission(id, PrivilegeTypeEnum.DELETABLE)) {
            throw new CustomerException("has not permission,to delete objective must be owner");
        }
        Objective objective = this.objectiveDAO.selectByPrimaryKey(id);
        if (objective == null) {
            throw new CustomerException(" the objective is not exist");
        }
        int result = this.objectiveDAO.deleteByPrimaryKey(id);
        if (result > 0) {
            String logDesc = String.format("%s deleted objective %s.", getCurrentUser().getName(), objective.getName());
            this.insertLog(id, null, BussinessLogOperateTypeEnum.DELETE, logDesc);
        }
        return result > 0;
    }


    @Override
    public ObjectiveDTO getObjectiveById(Long id) {
        Objective objective = this.objectiveDAO.selectByPrimaryKey(id);
        if (objective == null) {
            return new ObjectiveDTO();
        } else {
            ObjectiveDTO objectiveDTO = new ObjectiveDTO();
            BeanUtils.copyProperties(objective, objectiveDTO);
            combineKeyResultForObjective(objectiveDTO, getCurrentUser());
            combinePrivilege(objectiveDTO);
            if (objectiveDTO.getLevel().equals(ObjectiveLevelEnum.TEAM)) {
                Team team = this.teamDAO.selectByPrimaryKey(objectiveDTO.getTeamId());
                if (team != null) {
                    objectiveDTO.setGroupName(team.getName());
                }

            }
            return objectiveDTO;
        }
    }

    @Override
    public boolean closeObjective(Long objectiveId, CloseObjectiveRequestDTO requestDTO) {
        if (!hasPermission(objectiveId, PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException("has not permission,to close objective must be owner");
        }
        Objective objective = this.objectiveDAO.selectByPrimaryKey(objectiveId);
        if (objective == null) {
            throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_ID_NOT_EXIST);
        } else {
            if (objective.getStatus() == ObjectiveStatusEnum.OPEN) {
                BeanUtils.copyProperties(requestDTO, objective);
                objective.setStatus(ObjectiveStatusEnum.CLOSED);
                objective.setId(objectiveId);
                UserInfoDTO userInfoDTO = getCurrentUser();
                objective.setConfirmUserId(userInfoDTO.getId());
                objective.setConfirmUserName(userInfoDTO.getName());
                objective.setConfirmComment(requestDTO.getComment());
                objective.setConfirmFinalResult(requestDTO.getConfirmFinalResult());
                objective.setConfirmAt(new Date());
                objective.initializeForUpdate();
                this.objectiveDAO.updateByPrimaryKeySelective(objective);
                String logDesc = String.format("%s closed objective %s.", getCurrentUser().getName(), objective.getName());
                this.insertLog(objectiveId, objective.getOrganizationId(), BussinessLogOperateTypeEnum.CLOSE, logDesc);
                return true;
            } else {
                throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_STATUS_ERROR_NOT_CLOSE);
            }
        }
    }

    @Override
    public boolean reOpenObjective(Long id) {
        if (!hasPermission(id, PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException("has not permission,to reopen objective must be owner");
        }
        Objective objective = this.objectiveDAO.selectByPrimaryKey(id);
        if (objective == null) {
            throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_ID_NOT_EXIST);
        } else {
            if (objective.getStatus() == ObjectiveStatusEnum.CLOSED) {
                objective.setStatus(ObjectiveStatusEnum.OPEN);
                objective.initializeForUpdate();
                this.objectiveDAO.updateByPrimaryKeySelective(objective);
                String logDesc = String.format("%s reopened objective  %s.", getCurrentUser().getName(), objective.getName());
                this.insertLog(id, objective.getOrganizationId(), BussinessLogOperateTypeEnum.REOPEN, logDesc);
                return true;
            } else {
                throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_STATUS_ERROR_NOT_OPEN);
            }
        }
    }





    /*=========================================Objective aliment operate ===================================================*/
    @Override
    public void linkedParentObjective(Long objectiveId, Long linkedObjectiveId) {
        if (!hasPermission(objectiveId, PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException("has not permission,to link objective must be owner");
        }
        Objective objective = this.objectiveDAO.selectByPrimaryKey(objectiveId);
        Objective linked = this.objectiveDAO.selectByPrimaryKey(linkedObjectiveId);
        if (objective == null || linked == null) {
            throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_ID_NOT_EXIST);
        }
        objective.setLinkedObjectiveId(linkedObjectiveId);
        objective.setAlignment(String.valueOf(linkedObjectiveId));
        objective.initializeForUpdate();
        this.objectiveDAO.updateByPrimaryKeySelective(objective);
        String logDesc = String.format("%s linked objective %s to %s.", getCurrentUser().getName(), objective.getName(), linked.getName());
        this.insertLog(objectiveId, objective.getOrganizationId(), BussinessLogOperateTypeEnum.LINK, logDesc);
    }

    @Override
    public void unLinkedParentObjective(Long objectiveId) {
        if (!hasPermission(objectiveId, PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException("has not permission,to unlink objective must be owner");
        }
        Objective objective = this.objectiveDAO.selectByPrimaryKey(objectiveId);
        if (objective == null) {
            throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_ID_NOT_EXIST);
        }
        objective.setLinkedObjectiveId(null);
        objective.setUpdatedAt(new Date());
        this.objectiveDAO.updateByPrimaryKey(objective);
        String logDesc = String.format("%s unlink objective %s.", getCurrentUser().getName(), objective.getName());
        this.insertLog(objectiveId, objective.getOrganizationId(), BussinessLogOperateTypeEnum.UNLINK, logDesc);
    }

    @Override
    public ObjectiveChildrenDTO listChildrenObjectives(Long objectiveId) {
        Objective objective = this.objectiveDAO.selectByPrimaryKey(objectiveId);
        if (objective == null) {
            throw new CustomerException("the objective not exist! ");
        }
        List<Objective> children = this.objectiveDAO.listChildren(objective.getId());
        List<ObjectiveDTO> orgOkrs = new ArrayList<>();
        List<ObjectiveDTO> memberOkrs = new ArrayList<>();
        Map<String, List<ObjectiveDTO>> teamOkrs = new HashMap<>();
        ObjectiveChildrenDTO objectiveChildrenDTO = new ObjectiveChildrenDTO();
        objectiveChildrenDTO.setOrgOkrs(orgOkrs);
        objectiveChildrenDTO.setTeamOkrs(teamOkrs);
        objectiveChildrenDTO.setMemberOkrs(memberOkrs);
        if (!CollectionUtils.isEmpty(children)) {
            List<ObjectiveDTO> result = batchTransEntity2DTO(children);
            combinePrivilege(result);
            combineObjectiveGroupNameForDTO(result);
            result.forEach(objectiveDTO -> {
                // combineKeyResultForObjective(objectiveDTO, getCurrentUser());
                //按照team分组
                switch (objectiveDTO.getLevel()) {
                    case MEMBER:
                        memberOkrs.add(objectiveDTO);
                        break;
                    case ORGANIZATION:
                        orgOkrs.add(objectiveDTO);
                        break;
                    case TEAM:
                        List<ObjectiveDTO> list = teamOkrs.get(objectiveDTO.getGroupName());
                        if (list == null) {
                            list = new ArrayList<>();
                            if (objectiveDTO.getGroupName() != null) {
                                teamOkrs.put(objectiveDTO.getGroupName(), list);
                            }
                        }
                        list.add(objectiveDTO);
                        break;
                }
            });
        }
        return objectiveChildrenDTO;
    }

    @Override
    public List<ObjectiveDTO> listObjectiveLinkable(Long id, Long orgId) {
        return BeanConvertUtils.batchTransform(ObjectiveDTO.class, this.objectiveDAO.listOpenObjectiveByUser(id, orgId, new Date()));
    }

    @Override
    public List<ObjectiveTreeNodeDTO> listObjectiveTreeViewLinkable(Long id, Long organizationId) {
        List<ObjectiveTreeNodeDTO> rawList = BeanConvertUtils.batchTransform(ObjectiveTreeNodeDTO.class, this.objectiveDAO.listOpenObjectiveByUser(id, organizationId, new Date()));
        return buildTree(rawList);
    }

    @Override
    public AlignmentObjectiveDTO listObjectiveAlignment(Long organizationId, ListUserObjectiveAlignmentDTO level) {
        AlignmentObjectiveDTO dto = new AlignmentObjectiveDTO();
        switch (level.getLevel()) {
            case ORGANIZATION:
                dto.setOrganizationObjectives(BeanConvertUtils.batchTransform(ObjectiveDTO.class, this.objectiveDAO.listOrgObjectiveByLevel(organizationId, ObjectiveLevelEnum.ORGANIZATION.name())));
                dto.setMemberObjectives(new ArrayList<>());
                dto.setTeamObjectives(new ArrayList<>());
                break;
            case TEAM:
                dto.setOrganizationObjectives(BeanConvertUtils.batchTransform(ObjectiveDTO.class, this.objectiveDAO.listOrgObjectiveByLevel(organizationId, ObjectiveLevelEnum.ORGANIZATION.name())));
                dto.setTeamObjectives(BeanConvertUtils.batchTransform(ObjectiveDTO.class, this.objectiveDAO.listOrgObjectiveByLevel(organizationId, ObjectiveLevelEnum.TEAM.name())));
                dto.setMemberObjectives(new ArrayList<>());
                break;
            case MEMBER:
                dto.setOrganizationObjectives(BeanConvertUtils.batchTransform(ObjectiveDTO.class, this.objectiveDAO.listOrgObjectiveByLevel(organizationId, ObjectiveLevelEnum.ORGANIZATION.name())));
                dto.setTeamObjectives(BeanConvertUtils.batchTransform(ObjectiveDTO.class, this.objectiveDAO.listOrgObjectiveByLevel(organizationId, ObjectiveLevelEnum.TEAM.name())));
                dto.setMemberObjectives(BeanConvertUtils.batchTransform(ObjectiveDTO.class, this.objectiveDAO.listOrgObjectiveByLevel(organizationId, ObjectiveLevelEnum.MEMBER.name())));
                break;
        }

        return dto;
    }


    /*=========================================Objective complex query ===================================================*/
    @Override
    public PageInfo<ObjectiveDTO> listObjectiveByLevelFilter(ListObjectiveDTO queryDTO) {
        if (queryDTO.getEnd().before(queryDTO.getStart())) {
            throw new CustomerException("the start time must before the end time");
        }
        PageHelper.startPage(queryDTO.getPageNumber(), queryDTO.getPageSize());
        List<Objective> list = this.objectiveDAO.listObjective(queryDTO);
        PageInfo pageInfo = new PageInfo(list);
        if (!CollectionUtils.isEmpty(list)) {
            List<ObjectiveDTO> result = batchTransEntity2DTO(list);
            combinePrivilege(result);
            combineObjectiveGroupNameForDTO(result);
            pageInfo.setList(result);
        }
        return pageInfo;
    }

    @Override
    public ObjectiveStaticAndListGroupByLevelDTO analysisObjectiveStatisticsAndListByLevel(ObjectiveStaticAndListQueryDTO query) {
        if (query.getEnd().before(query.getStart())) {
            throw new CustomerException("the start time must before the end time");
        }
        ObjectiveStaticAndListGroupByLevelDTO result = new ObjectiveStaticAndListGroupByLevelDTO();
        List<Objective> objectives = this.objectiveDAO.listObjectivesByFilter(query);
        //获取org所有的team
        List<TeamStatisticsDTO> teams = this.teamDAO.listTeamsStatisticsByOrganization(query.getOrganizationId());
        Map<Long, String> teamMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(teams)) {
            for (TeamStatisticsDTO team : teams) {
                if (team.getId() != null) {
                    teamMap.put(team.getId(), team.getName());
                }
            }
        }

        if (CollectionUtils.isEmpty(objectives)) {
            result.setMemberObjectives(new ArrayList<>());
            result.setMemberStatistics(new ObjectiveStatisticsDTO());
            result.setOrganizationObjectives(new ArrayList<>());
            result.setOrganizationStatistics(new ObjectiveStatisticsDTO());
            result.setTeamObjectives(new HashMap<>());
            result.setTeamStatistics(new ObjectiveStatisticsDTO());
        } else {
            List<ObjectiveDTO> totalObjective = batchTransEntity2DTO(objectives);
            combinePrivilege(totalObjective);
            List<ObjectiveDTO> orgList = new ArrayList<>();
            Map<String, List<ObjectiveDTO>> teamOkrMap = new HashMap<>();
            List<ObjectiveDTO> memberList = new ArrayList<>();
            Integer org_on_track = 0, org_off_track = 0, org_at_risk = 0, org_exceeded = 0;
            Integer team_on_track = 0, team_off_track = 0, team_at_risk = 0, team_exceeded = 0;
            Integer member_on_track = 0, member_off_track = 0, member_at_risk = 0, member_exceeded = 0;
            Integer orgNum = 0;
            Integer teamNum = 0;
            Integer memberNum = 0;
            Double orgProgress = 0.0;
            Double teamProgress = 0.0;
            Double memberProgress = 0.0;
            for (ObjectiveDTO dto : totalObjective) {
                switch (dto.getLevel()) {
                    case TEAM:
                        List<ObjectiveDTO> teamList = teamOkrMap.get(teamMap.get(dto.getTeamId()));
                        if (teamList == null) {
                            teamList = new ArrayList<>();
                            if ((teamMap.get(dto.getTeamId())) != null) {
                                teamOkrMap.put(teamMap.get(dto.getTeamId()), teamList);
                            }

                        }
                        dto.setGroupName(teamMap.get(dto.getTeamId()));
                        teamList.add(dto);
                        switch (dto.getBusinessStatus()) {
                            case ON_TRACK:
                                team_on_track++;
                                break;
                            case OFF_TRACK:
                                team_off_track++;
                                break;
                            case AT_RISK:
                                team_at_risk++;
                                break;
                            case EXCEEDED:
                                team_exceeded++;
                                break;
                        }
                        teamNum = teamNum + 1;
                        teamProgress = teamProgress + dto.getProgress();
                        break;
                    case MEMBER:
                        memberList.add(dto);
                        switch (dto.getBusinessStatus()) {
                            case ON_TRACK:
                                member_on_track++;
                                break;
                            case OFF_TRACK:
                                member_off_track++;
                                break;
                            case AT_RISK:
                                member_at_risk++;
                                break;
                            case EXCEEDED:
                                member_exceeded++;
                                break;
                        }
                        memberNum = memberNum + 1;
                        memberProgress = memberProgress + dto.getProgress();
                        break;
                    case ORGANIZATION:
                        orgList.add(dto);
                        switch (dto.getBusinessStatus()) {
                            case ON_TRACK:
                                org_on_track++;
                                break;
                            case OFF_TRACK:
                                org_off_track++;
                                break;
                            case AT_RISK:
                                org_at_risk++;
                                break;
                            case EXCEEDED:
                                org_exceeded++;
                                break;
                        }
                        orgNum = orgNum + 1;
                        orgProgress = orgProgress + dto.getProgress();
                        break;
                }

            }
            Double orgAvgProgress = 0.0;
            Double teamAvgProgress = 0.0;
            Double memberAvgProgress = 0.0;
            if (orgNum > 0) {
                orgAvgProgress = Double.valueOf(df.format(orgProgress / orgNum));
            }
            if (teamNum > 0) {
                teamAvgProgress = Double.valueOf(df.format(teamProgress / teamNum));
            }
            if (memberNum > 0) {
                memberAvgProgress = Double.valueOf(df.format(memberProgress / memberNum));
            }
            ObjectiveStatisticsDTO orgStatistics = new ObjectiveStatisticsDTO(org_on_track, org_off_track, org_at_risk, org_exceeded, orgAvgProgress);
            ObjectiveStatisticsDTO teamStatistics = new ObjectiveStatisticsDTO(team_on_track, team_off_track, team_at_risk, team_exceeded, teamAvgProgress);
            ObjectiveStatisticsDTO memberStatistics = new ObjectiveStatisticsDTO(member_on_track, member_off_track, member_at_risk, member_exceeded, memberAvgProgress);
            result.setOrganizationStatistics(orgStatistics);
            result.setOrganizationObjectives(orgList);
            result.setTeamStatistics(teamStatistics);
            result.setTeamObjectives(teamOkrMap);
            result.setMemberStatistics(memberStatistics);
            result.setMemberObjectives(memberList);
            //给organization和team的okr绑定组织和team的name
            if (!CollectionUtils.isEmpty(result.getOrganizationObjectives())) {
                combineObjectiveGroupNameForDTO(result.getOrganizationObjectives());
            }
        }
        return result;
    }


    @Override
    public List<ObjectiveTreeNodeDTO> listObjectiveTreeView(ObjectiveStaticAndListQueryDTO query) {
        if (query.getEnd().before(query.getStart())) {
            throw new CustomerException("the start time must before the end time");
        }
        List<ObjectiveTreeNodeDTO> rawList = batchTransEntity2TreeDTO(this.objectiveDAO.listObjectivesByFilter(query));
        //绑定objective的组织和team的name
        combineObjectiveGroupName(rawList);
        return buildTree(rawList);
    }

    @Override
    public PageInfo<ObjectiveKeyResultDTO> listKeyResultsForObjective(ListObjectiveKeyResultPageRequestDTO request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ObjectiveKeyResultDTO> list = this.keyResultDAO.listObjectiveKeyResultCombineCommentNumByObjectiveId(request.getObjectiveId());
        PageInfo pageInfo = new PageInfo(list);
        List<ObjectiveKeyResultDTO> krList = BeanConvertUtils.batchTransform(ObjectiveKeyResultDTO.class, list);
        UserInfoDTO user = getCurrentUser();
        if(!CollectionUtils.isEmpty(krList)){
            krList.forEach(keyResultDTO -> {
                combineKeyResultPrivilege(keyResultDTO,user);
            });
        }
        pageInfo.setList(krList);
        return pageInfo;
    }


    @Override
    public PageInfo<ObjectiveTimelineDTO> listObjectiveOperateTimeline(ListObjectiveTimeLineQueryDTO queryDTO) {
        return this.listLog(queryDTO.getObjectiveId(), queryDTO.getPageNumber(), queryDTO.getPageSize());
    }


    @Override
    public ObjectiveStatisticsDTO countObjectiveNumByStatusAndListOverTimeDataByLevelFilter(CountObjectiveByStatusDTO queryDTO) {
        if (queryDTO.getEnd().before(queryDTO.getStart())) {
            throw new CustomerException("the start time must before the end time");
        }
        ObjectiveStatisticsDTO objectiveStatisticsDTO = this.objectiveDAO.countObjectiveByStatus(queryDTO);
        if (objectiveStatisticsDTO == null) {
            return new ObjectiveStatisticsDTO();
        }
        objectiveStatisticsDTO.setProgressOverTime(this.objectiveDAO.countObjectiveOverTimeProgressByStatus(queryDTO));
        return objectiveStatisticsDTO;
    }

    @Override
    public ObjectiveWeeklyPlanRegularEmailReportDTO prepareDataForOrgOKRRegularReport(GetOrgObjectiveRegularReportDTO query, Set<Long> filters) {
        ObjectiveWeeklyPlanRegularEmailReportDTO emailReportDTO = new ObjectiveWeeklyPlanRegularEmailReportDTO();
        Integer Quarter = DateUtil.getQuarter();
        Integer year = DateUtil.getYear();
        emailReportDTO.setUserName(query.getUserName());
        emailReportDTO.setReportTime("Q" + Quarter + " " + year);
        GetUserObjectiveReportDTO okrQuery = new GetUserObjectiveReportDTO();
        okrQuery.setOrganizationId(query.getOrganizationId());
        okrQuery.setUserId(query.getUserId());
        okrQuery.setStart(DateUtil.getQuarterStartTime(new Date()));
        okrQuery.setEnd(DateUtil.getQuarterEndTime(new Date()));
        List<Objective> objectives = this.objectiveDAO.listAllObjectives(okrQuery);

        MemberObjectiveRegularEmailReportDTO orgRegularEmailReport = new MemberObjectiveRegularEmailReportDTO();
        List<MemberObjectiveRegularEmailReportDTO> teamsRegularEmailReport = new ArrayList<>();
        List<MemberObjectiveRegularEmailReportDTO> memberRegularEmailReport = new ArrayList<>();
        emailReportDTO.setOrgOkrs(orgRegularEmailReport);
        emailReportDTO.setTeamOkrs(teamsRegularEmailReport);
        emailReportDTO.setMemberOkrs(memberRegularEmailReport);

        Map<Long, List<ObjectiveDTO>> orgOkrMap = new HashMap<>();
        Map<Long, List<ObjectiveDTO>> teamOkrMap = new HashMap<>();
        Map<Long, List<ObjectiveDTO>> memberOkrMap = new HashMap<>();

        Integer org_on_track = 0, org_off_track = 0, org_at_risk = 0, org_exceeded = 0;
        Integer team_on_track = 0, team_off_track = 0, team_at_risk = 0, team_exceeded = 0;
        Integer member_on_track = 0, member_off_track = 0, member_at_risk = 0, member_exceeded = 0;
        Integer orgNum = 0;
        Integer teamNum = 0;
        Integer memberNum = 0;
        Double orgProgress = 0.0;
        Double teamProgress = 0.0;
        Double memberProgress = 0.0;

        if (CollectionUtils.isEmpty(objectives)) {
            return emailReportDTO;
        } else {
            List<ObjectiveDTO> totalObjective = batchTransEntity2DTO(objectives);
            combinePrivilege(totalObjective);
            for (ObjectiveDTO dto : totalObjective) {
                switch (dto.getLevel()) {
                    case TEAM:
                        if (filters.contains(dto.getId())) {
                            if (teamOkrMap.get(dto.getTeamId()) == null) {
                                List<ObjectiveDTO> list = new ArrayList<>();
                                list.add(dto);
                                teamOkrMap.put(dto.getTeamId(), list);
                            } else {
                                teamOkrMap.get(dto.getTeamId()).add(dto);
                            }
                        }
                        switch (dto.getBusinessStatus()) {
                            case ON_TRACK:
                                team_on_track++;
                                break;
                            case OFF_TRACK:
                                team_off_track++;
                                break;
                            case AT_RISK:
                                team_at_risk++;
                                break;
                            case EXCEEDED:
                                team_exceeded++;
                                break;
                        }
                        teamNum = teamNum + 1;
                        teamProgress = teamProgress + dto.getProgress();
                        break;
                    case MEMBER:
                        if (filters.contains(dto.getId())) {
                            if (memberOkrMap.get(dto.getOwnerId()) == null) {
                                List<ObjectiveDTO> list = new ArrayList<>();
                                list.add(dto);
                                memberOkrMap.put(dto.getOwnerId(), list);
                            } else {
                                memberOkrMap.get(dto.getOwnerId()).add(dto);
                            }
                        }
                        switch (dto.getBusinessStatus()) {
                            case ON_TRACK:
                                member_on_track++;
                                break;
                            case OFF_TRACK:
                                member_off_track++;
                                break;
                            case AT_RISK:
                                member_at_risk++;
                                break;
                            case EXCEEDED:
                                member_exceeded++;
                                break;
                        }
                        memberNum = memberNum + 1;
                        memberProgress = memberProgress + dto.getProgress();
                        break;
                    case ORGANIZATION:
                        if (filters.contains(dto.getId())) {
                            if (orgOkrMap.get(dto.getOrganizationId()) == null) {
                                List<ObjectiveDTO> list = new ArrayList<>();
                                list.add(dto);
                                orgOkrMap.put(dto.getOrganizationId(), list);
                            } else {
                                orgOkrMap.get(dto.getOrganizationId()).add(dto);
                            }
                        }
                        switch (dto.getBusinessStatus()) {
                            case ON_TRACK:
                                org_on_track++;
                                break;
                            case OFF_TRACK:
                                org_off_track++;
                                break;
                            case AT_RISK:
                                org_at_risk++;
                                break;
                            case EXCEEDED:
                                org_exceeded++;
                                break;
                        }
                        orgNum = orgNum + 1;
                        orgProgress = orgProgress + dto.getProgress();
                        break;
                }

            }
            String orgAvgProgress = "0%";
            String teamAvgProgress = "0%";
            String memberAvgProgress = "0%";
            if (orgNum > 0) {
                orgAvgProgress = "" + df2.format(Double.valueOf(df.format(orgProgress / orgNum)) * 100) + "%";
            }
            if (teamNum > 0) {
                teamAvgProgress = "" + df2.format(Double.valueOf(df.format(teamProgress / teamNum)) * 100) + "%";
            }
            if (memberNum > 0) {
                memberAvgProgress = "" + df2.format(Double.valueOf(df.format(memberProgress / memberNum)) * 100) + "%";
            }


            emailReportDTO.setOrgAvgProgress(orgAvgProgress);
            emailReportDTO.setTeamAvgProgress(teamAvgProgress);
            emailReportDTO.setMemberAvgProgress(memberAvgProgress);

            //构建org的okr report email
            if (!CollectionUtils.isEmpty(orgOkrMap)) {
                Map.Entry<Long, List<ObjectiveDTO>> entry = orgOkrMap.entrySet().iterator().next();
                Long orgId = entry.getKey();
                List<ObjectiveDTO> orgOkrs = entry.getValue();
                Organization org = this.organizationDAO.selectByPrimaryKey(orgId);
                if (org != null) {
                    orgRegularEmailReport.setName(org.getName());
                }
                orgRegularEmailReport.setOkrs(batchTranaDTO2ObjectiveConbineWeeklyPlanDTO(orgOkrs));
                Integer orgItemOkrNum = 0;
                Integer orgItemKRNum = 0;
                Double totalProgress = 0.0;
                Double avgProgress = 0.0;
                for (ObjectiveDTO orgOkrItem : orgOkrs) {
                    orgItemOkrNum = orgItemOkrNum + 1;
                    orgItemKRNum = orgItemKRNum + (orgOkrItem.getKeyResults() != null ? orgOkrItem.getKeyResults().size() : 0);
                    totalProgress = totalProgress + orgOkrItem.getProgress();
                }
                if (orgItemOkrNum != 0) {
                    avgProgress = Double.valueOf(df.format(totalProgress / orgItemOkrNum)) * 100;
                }
                orgRegularEmailReport.setAvgProgress(avgProgress);
                orgRegularEmailReport.setOkrNum(orgItemOkrNum);
                orgRegularEmailReport.setKrNum(orgItemKRNum);
            }

            //构造team的okr report email
            if (!CollectionUtils.isEmpty(teamOkrMap)) {
                for (Map.Entry<Long, List<ObjectiveDTO>> entry : teamOkrMap.entrySet()) {
                    MemberObjectiveRegularEmailReportDTO teamRegularEmailReport = new MemberObjectiveRegularEmailReportDTO();
                    Long teamId = entry.getKey();
                    List<ObjectiveDTO> orgOkrs = entry.getValue();
                    Team team = this.teamDAO.selectByPrimaryKey(teamId);
                    if (team != null) {
                        teamRegularEmailReport.setName(team.getName());
                    }
                    teamRegularEmailReport.setOkrs(batchTranaDTO2ObjectiveConbineWeeklyPlanDTO(orgOkrs));
                    Integer teamItemOkrNum = 0;
                    Integer teamItemKRNum = 0;
                    Double totalProgress = 0.0;
                    Double avgProgress = 0.0;
                    for (ObjectiveDTO teamOkrItem : orgOkrs) {
                        teamItemOkrNum = teamItemOkrNum + 1;
                        teamItemKRNum = teamItemKRNum + (teamOkrItem.getKeyResults() != null ? teamOkrItem.getKeyResults().size() : 0);
                        totalProgress = totalProgress + teamOkrItem.getProgress();
                    }
                    if (teamItemOkrNum != 0) {
                        avgProgress = Double.valueOf(df.format(totalProgress / teamItemOkrNum)) * 100;
                    }
                    teamRegularEmailReport.setAvgProgress(avgProgress);
                    teamRegularEmailReport.setOkrNum(teamItemOkrNum);
                    teamRegularEmailReport.setKrNum(teamItemKRNum);
                    emailReportDTO.getTeamOkrs().add(teamRegularEmailReport);
                }
            }

            //构造member的okr report email
            if (!CollectionUtils.isEmpty(memberOkrMap)) {
                for (Map.Entry<Long, List<ObjectiveDTO>> entry : memberOkrMap.entrySet()) {
                    MemberObjectiveRegularEmailReportDTO memberObjectiveRegularEmailReport = new MemberObjectiveRegularEmailReportDTO();
                    List<ObjectiveDTO> orgOkrs = entry.getValue();
                    memberObjectiveRegularEmailReport.setOkrs(batchTranaDTO2ObjectiveConbineWeeklyPlanDTO(orgOkrs));
                    Integer memberItemOkrNum = 0;
                    Integer memberItemKRNum = 0;
                    Double totalProgress = 0.0;
                    Double avgProgress = 0.0;
                    for (ObjectiveDTO memberOkrItem : orgOkrs) {
                        memberItemOkrNum = memberItemOkrNum + 1;
                        memberItemKRNum = memberItemKRNum + (memberOkrItem.getKeyResults() != null ? memberOkrItem.getKeyResults().size() : 0);
                        totalProgress = totalProgress + memberOkrItem.getProgress();
                    }
                    if (memberItemOkrNum != 0) {
                        avgProgress = Double.valueOf(df.format(totalProgress / memberItemOkrNum)) * 100;
                    }
                    memberObjectiveRegularEmailReport.setName(orgOkrs.get(0).getOwnerName());
                    memberObjectiveRegularEmailReport.setAvgProgress(avgProgress);
                    memberObjectiveRegularEmailReport.setOkrNum(memberItemOkrNum);
                    memberObjectiveRegularEmailReport.setKrNum(memberItemOkrNum);
                    emailReportDTO.getMemberOkrs().add(memberObjectiveRegularEmailReport);
                }
            }

        }
        return emailReportDTO;
    }


    @Override
    public List<ObjectiveDTO> listObjectiveWhichWeeklyPlanLinked(Long id) {
        List<ObjectiveDTO> objectives = BeanConvertUtils.batchTransform(ObjectiveDTO.class, this.objectiveDAO.listObjectiveByPlan(id));
        if (!CollectionUtils.isEmpty(objectives)) {
            objectives.forEach(objectiveDTO -> {
                combineKeyResultForObjective(objectiveDTO, getCurrentUser());
            });
        }
        combinePrivilege(objectives);
        return objectives;
    }

    @Override
    public Integer sumOrganizationObjective(Long id) {
        return this.objectiveDAO.countOrganizationObjective(id);
    }

    @Override
    public ObjectiveCountReportDTO analysisOrgObjectiveTotalReport(Date start, Date end) {
        return this.objectiveDAO.getTotalObjectiveReport(start, end);
    }

    @Override
    public WeeklyPlanDTO getObjectvieCombineWeeklyPlan(Long objectiveId, Integer week) {
        ObjectiveDTO objective = this.getObjectiveById(objectiveId);
        if(objective==null||objective.getId()==null){
            throw new CustomerException("the Objective not esist!");
        }
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        ListWeeklyPlanDTO listWeeklyPlanQuery=new ListWeeklyPlanDTO();
        listWeeklyPlanQuery.setOrganizationId(objective.getOrganizationId());
        listWeeklyPlanQuery.setUserId(currentUser.getId());
        listWeeklyPlanQuery.setWeek(week);
        WeeklyPlanDTO weekPlan = weeklyPlanService.listUserWeeklyPlanByOrg(listWeeklyPlanQuery);
        return weekPlan;
    }


    /*=========================================Objective job schedule ===================================================*/
    @Transactional
    @Override
    public void updateObjectivePorgressDaily() {
        try {
            Date now = new Date();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.getDayBefore(now, -1));
            List<Objective> unCompleteObjective = this.objectiveDAO.listOpenObjective(now);
            if (!CollectionUtils.isEmpty(unCompleteObjective)) {
                for (Objective objective : unCompleteObjective) {
                    try {
                        List<KeyResult> keyResults = this.keyResultDAO.listObjectiveKeyResultByObjectiveId(objective.getId());
                        //重新调整Objective的status
                        calObjectiveStatus(objective, keyResults.size());
                        ObjectiveDalilyProgress dalilyProgress = new ObjectiveDalilyProgress();
                        dalilyProgress.setDate(date);
                        dalilyProgress.setObjectiveId(objective.getId());
                        dalilyProgress.setStatus(objective.getBusinessStatus());
                        dalilyProgress.setProgress(objective.getProgress());
                        dalilyProgress.setExpectProgress(calObjectExpectProgress(objective));
                        dalilyProgress.initializeForInsert();
                        this.objectiveDalilyProgressDAO.insertSelective(dalilyProgress);
                        this.objectiveDAO.updateByPrimaryKeySelective(objective);
                    } catch (Exception e) {
                        log.error(ExceptionUtil.stackTraceToString(e));
                    }
                }
            }
        } catch (Exception e2) {
            log.error(ExceptionUtil.stackTraceToString(e2));
        }


    }

    @Override
    public void recordOffTractObjectiveUpdateRemind() {
        List<Objective> offTractObjectives = this.objectiveDAO.listOffTrackObjective();
        if (!CollectionUtils.isEmpty(offTractObjectives)) {
            offTractObjectives.forEach(objective -> {
                try {
                    sendRemindToUpdateOffTrackObjective(objective);
                } catch (Exception e) {
                    log.error(ExceptionUtil.stackTraceToString(e));
                }
            });
        }
    }

    @Override
    @Transactional
    public void recordObjectiveLaskWeekProgress() {
        try {
            Date now = new Date();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.getDayBefore(now, -1));
            List<Objective> unCompleteObjective = this.objectiveDAO.listOpenObjective(now);
            if (!CollectionUtils.isEmpty(unCompleteObjective)) {
                for (Objective objective : unCompleteObjective) {
                    try {
                        objective.setLastProgress(objective.getProgress());
                        this.objectiveDAO.updateByPrimaryKeySelective(objective);
                    } catch (Exception e) {
                        log.error(ExceptionUtil.stackTraceToString(e));
                    }
                }
            }
        } catch (Exception e2) {
            log.error(ExceptionUtil.stackTraceToString(e2));
        }

    }


    /*=========================================Objective permission ===================================================*/
    @Override
    public Boolean hasPermission(Long objectiveId, PrivilegeTypeEnum typeEnum) {

        Objective objective = this.objectiveDAO.selectByPrimaryKey(objectiveId);
        if (objective == null) {
            return false;
        }

        ObjectiveDTO objectiveDTO = new ObjectiveDTO();
        BeanUtils.copyProperties(objective, objectiveDTO);
        List<MemberDTO> orgOwner = null;
        List<MemberDTO> teamOwner = null;
        switch (objectiveDTO.getLevel()) {
            case ORGANIZATION:
                orgOwner = this.memberService.listOrganizationMembers(objectiveDTO.getOrganizationId(), true);
                break;
            case TEAM:
                orgOwner = this.memberService.listOrganizationMembers(objectiveDTO.getOrganizationId(), true);
                teamOwner = this.memberService.listTeamMembers(objectiveDTO.getTeamId(), true);
                break;
            case MEMBER:
                break;
        }
        ObjectiveResourcesDesc desc = new ObjectiveResourcesDesc();
        desc.setObjective(objectiveDTO);
        desc.setOrgOwner(orgOwner);
        desc.setTeamOwner(teamOwner);

        UserInfoDTO currentUser = getCurrentUser();
        ObjectivePrivilege privilege = objectivePermissionRulesParser.getUserPrivilege(currentUser, desc);
        switch (typeEnum) {
            case DELETABLE:
                return privilege.getDeletable();
            case UPDATABLE:
                return privilege.getUpdatable();
            case KEY_RESULT_ADDABLE:
                return privilege.getKeyResultAddable();
        }
        return false;
    }


    /*=========================================Objective support ===================================================*/

    private void calObjectiveProgress(Objective objective) {
        List<KeyResult> keyResults = this.keyResultDAO.listObjectiveKeyResultByObjectiveId(objective.getId());
        if (!CollectionUtils.isEmpty(keyResults)) {
            double totalWeight = 0.0;
            double totalProgress = 0.0;
            for (int i = 0; i < keyResults.size(); i++) {
                KeyResult keyResult = keyResults.get(i);
                totalWeight += keyResult.getWeight();
                totalProgress = totalProgress + (keyResult.getProgress() * keyResult.getWeight());
            }
            Double progress = Double.valueOf(df.format(totalProgress / totalWeight));
            objective.setProgress(progress);
            calObjectiveStatus(objective, keyResults.size());
        } else {
            objective.setProgress(0.0);
            calObjectiveStatus(objective, keyResults.size());
        }
    }

    private void calObjectiveStatus(Objective objective, Integer krNum) {
        if (krNum.equals(0)) {
            objective.setProgress(0.0);
            objective.setBusinessStatus(ObjectiveBusinessStatusEnum.ON_TRACK);
            return;
        }
        Double expectProgress = calObjectExpectProgress(objective);
        if (expectProgress > 0.00) {
            //如果progress为1，表示已经实现，设置为EXCEEDED
            if (objective.getProgress().equals(1.0)) {
                objective.setBusinessStatus(ObjectiveBusinessStatusEnum.EXCEEDED);
                return;
            }
            //根据实际的progress和期望的progress计算businessStatus的值
            Double progressRate = Double.valueOf(df.format(objective.getProgress() / expectProgress));
            if (progressRate > EXCEEDED_RATE) {
                objective.setBusinessStatus(ObjectiveBusinessStatusEnum.EXCEEDED);
            } else if (progressRate > ON_TRACK_RATE) {
                objective.setBusinessStatus(ObjectiveBusinessStatusEnum.ON_TRACK);
            } else if (progressRate > AT_TRACK_RATE) {
                objective.setBusinessStatus(ObjectiveBusinessStatusEnum.AT_RISK);
            } else {
                objective.setBusinessStatus(ObjectiveBusinessStatusEnum.OFF_TRACK);
            }
        } else {
            //当前时间还没有到设定的开始结束时间，计算businessStatus的状态
            if (objective.getProgress() > 0) {
                objective.setBusinessStatus(ObjectiveBusinessStatusEnum.EXCEEDED);
            } else {
                objective.setBusinessStatus(ObjectiveBusinessStatusEnum.ON_TRACK);
            }
        }

    }

    private Double calObjectExpectProgress(Objective objective) {
        Date now = new Date();
        if (DateUtil.differentDays(objective.getPeriodStartTime(), objective.getPeriodEndTime()) == 0) {
            if (DateUtil.differentDays(objective.getPeriodStartTime(), now) >= 0) {
                return 1.00;
            } else {
                return 0.00;
            }
        }
        Double expectProgress = Double.valueOf(df.format(Double.valueOf(DateUtil.differentDays(objective.getPeriodStartTime(), now)) / Double.valueOf(DateUtil.differentDays(objective.getPeriodStartTime(), objective.getPeriodEndTime()))));
        return expectProgress > 0.00 ? expectProgress : 0.00;
    }


    private void combineObjectiveGroupName(List<ObjectiveTreeNodeDTO> rawList) {
        Map<Long, String> orgMap = new HashMap<>();
        Map<Long, String> teamMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(rawList)) {
            for (ObjectiveTreeNodeDTO node : rawList) {
                switch (node.getLevel()) {
                    case ORGANIZATION:
                        Long orgId = node.getOrganizationId();
                        String orgName = orgMap.get(orgId);
                        if (orgName == null) {
                            Organization org = this.organizationDAO.selectByPrimaryKey(orgId);
                            if (org != null) {
                                orgMap.put(orgId, org.getName());
                                orgName = org.getName();
                            } else {
                                orgName = "";
                            }

                        }
                        node.setGroupName(orgName);
                        break;
                    case TEAM:
                        Long teamId = node.getTeamId();
                        String teamName = teamMap.get(teamId);
                        if (teamName == null) {
                            Team team = this.teamDAO.selectByPrimaryKey(teamId);
                            if (team != null) {
                                teamMap.put(teamId, team.getName());
                                teamName = team.getName();
                            } else {
                                teamName = "";
                            }
                        }
                        node.setGroupName(teamName);
                        break;
                    case MEMBER:
                        node.setGroupName(node.getOwnerName());
                        break;
                }

            }
        }
    }

    private void combineObjectiveGroupNameForDTO(List<ObjectiveDTO> rawList) {
        Map<Long, String> orgMap = new HashMap<>();
        Map<Long, String> teamMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(rawList)) {
            for (ObjectiveDTO node : rawList) {
                switch (node.getLevel()) {
                    case ORGANIZATION:
                        Long orgId = node.getOrganizationId();
                        String orgName = orgMap.get(orgId);
                        if (orgName == null) {
                            Organization org = this.organizationDAO.selectByPrimaryKey(orgId);
                            if (org != null) {
                                orgMap.put(orgId, org.getName());
                                orgName = org.getName();
                            } else {
                                orgName = "";
                            }

                        }
                        node.setGroupName(orgName);
                        break;
                    case TEAM:
                        Long teamId = node.getTeamId();
                        String teamName = teamMap.get(teamId);
                        if (teamName == null) {
                            Team team = this.teamDAO.selectByPrimaryKey(teamId);
                            if (team != null) {
                                teamMap.put(teamId, team.getName());
                                teamName = team.getName();
                            } else {
                                teamName = "";
                            }
                        }
                        node.setGroupName(teamName);
                        break;
                    case MEMBER:
                        node.setGroupName(node.getOwnerName());
                        break;
                }

            }
        }
    }

    private void combinePrivilege(ObjectiveDTO objective) {
        UserInfoDTO user = getCurrentUser();
        ObjectivePrivilege privilege = null;
        switch (objective.getLevel()) {
            case ORGANIZATION:
                ObjectiveResourcesDesc desc = new ObjectiveResourcesDesc();
                desc.setObjective(objective);
                desc.setOrgOwner(this.memberService.listOrganizationMembers(objective.getOrganizationId(), true));
                privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc);
                break;
            case TEAM:
                ObjectiveResourcesDesc desc2 = new ObjectiveResourcesDesc();
                desc2.setObjective(objective);
                desc2.setOrgOwner(this.memberService.listOrganizationMembers(objective.getOrganizationId(), true));
                desc2.setTeamOwner(this.memberService.listTeamMembers(objective.getTeamId(), true));
                privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc2);
                break;
            case MEMBER:
                ObjectiveResourcesDesc desc3 = new ObjectiveResourcesDesc();
                desc3.setObjective(objective);
                privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc3);
        }
        objective.setPrivilege(privilege);
        boolean hasObjectivePermission = privilege.getUpdatable();
        if (!CollectionUtils.isEmpty(objective.getKeyResults())) {
            for (ObjectiveKeyResultDTO kr : objective.getKeyResults()) {
                if (hasObjectivePermission) {
                    kr.setPrivilege(keyResultPermissionRulesParser.getFullPrivilege());
                } else {
                    kr.setPrivilege(keyResultPermissionRulesParser.getUserPrivilege(user, kr));
                }
            }
        }
    }

    private void combineObjectiveTreeNodeDTOPrivilege(List<ObjectiveTreeNodeDTO> list) {
        UserInfoDTO user = getCurrentUser();
        List<MemberDTO> orgOwners = null;
        Map<Long, List<MemberDTO>> teamOwners = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (ObjectiveTreeNodeDTO objectiveTree : list) {
                ObjectiveDTO objective = new ObjectiveDTO();
                BeanUtils.copyProperties(objectiveTree, objective);
                objective.setKeyResults(objectiveTree.getKeyResults());
                ObjectivePrivilege privilege = null;
                switch (objective.getLevel()) {
                    case ORGANIZATION:
                        if (orgOwners == null) {
                            orgOwners = this.memberService.listOrganizationMembers(objective.getOrganizationId(), true);
                        }
                        ObjectiveResourcesDesc desc = new ObjectiveResourcesDesc();
                        desc.setObjective(objective);
                        desc.setOrgOwner(orgOwners);
                        privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc);
                        break;
                    case TEAM:
                        if (orgOwners == null) {
                            orgOwners = this.memberService.listOrganizationMembers(objective.getOrganizationId(), true);
                        }
                        List<MemberDTO> teamOwner = teamOwners.get(objective.getTeamId());
                        if (teamOwner == null) {
                            teamOwner = this.memberService.listTeamMembers(objective.getTeamId(), true);
                            teamOwners.put(objective.getTeamId(), teamOwner);
                        }
                        ObjectiveResourcesDesc desc2 = new ObjectiveResourcesDesc();
                        desc2.setObjective(objective);
                        desc2.setOrgOwner(orgOwners);
                        desc2.setTeamOwner(teamOwner);
                        privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc2);
                        break;
                    case MEMBER:
                        ObjectiveResourcesDesc desc3 = new ObjectiveResourcesDesc();
                        desc3.setObjective(objective);
                        privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc3);
                }
                objectiveTree.setPrivilege(privilege);
                boolean hasObjectivePermission = privilege.getUpdatable();
                if (!CollectionUtils.isEmpty(objectiveTree.getKeyResults())) {
                    for (ObjectiveKeyResultDTO kr : objectiveTree.getKeyResults()) {
                        if (hasObjectivePermission) {
                            kr.setPrivilege(keyResultPermissionRulesParser.getFullPrivilege());
                        } else {
                            kr.setPrivilege(keyResultPermissionRulesParser.getUserPrivilege(user, kr));
                        }
                    }
                }

            }
        }
    }

    private void combinePrivilege(List<ObjectiveDTO> list) {
        UserInfoDTO user = getCurrentUser();
        List<MemberDTO> orgOwners = null;
        Map<Long, List<MemberDTO>> teamOwners = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (ObjectiveDTO objective : list) {
                ObjectivePrivilege privilege = null;
                switch (objective.getLevel()) {
                    case ORGANIZATION:
                        if (orgOwners == null) {
                            orgOwners = this.memberService.listOrganizationMembers(objective.getOrganizationId(), true);
                        }
                        ObjectiveResourcesDesc desc = new ObjectiveResourcesDesc();
                        desc.setObjective(objective);
                        desc.setOrgOwner(orgOwners);
                        privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc);
                        break;
                    case TEAM:
                        if (orgOwners == null) {
                            orgOwners = this.memberService.listOrganizationMembers(objective.getOrganizationId(), true);
                        }
                        List<MemberDTO> teamOwner = teamOwners.get(objective.getTeamId());
                        if (teamOwner == null) {
                            teamOwner = this.memberService.listTeamMembers(objective.getTeamId(), true);
                            teamOwners.put(objective.getTeamId(), teamOwner);
                        }
                        ObjectiveResourcesDesc desc2 = new ObjectiveResourcesDesc();
                        desc2.setObjective(objective);
                        desc2.setOrgOwner(orgOwners);
                        desc2.setTeamOwner(teamOwner);
                        privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc2);
                        break;
                    case MEMBER:
                        ObjectiveResourcesDesc desc3 = new ObjectiveResourcesDesc();
                        desc3.setObjective(objective);
                        privilege = this.objectivePermissionRulesParser.getUserPrivilege(user, desc3);
                }
                objective.setPrivilege(privilege);
                boolean hasObjectivePermission = privilege.getUpdatable();
                if (!CollectionUtils.isEmpty(objective.getKeyResults())) {
                    for (ObjectiveKeyResultDTO kr : objective.getKeyResults()) {
                        if (hasObjectivePermission) {
                            kr.setPrivilege(keyResultPermissionRulesParser.getFullPrivilege());
                        } else {
                            kr.setPrivilege(keyResultPermissionRulesParser.getUserPrivilege(user, kr));
                        }
                    }
                }

            }

        }
    }


    private void combineKeyResultPrivilege(ObjectiveKeyResultDTO keyResultDTO, UserInfoDTO user) {
        keyResultService.combinePrivilege(keyResultDTO);
    }


    private void combineKeyResultForObjective(ObjectiveDTO dto, UserInfoDTO user) {
        ListObjectiveKeyResultPageRequestDTO request = new ListObjectiveKeyResultPageRequestDTO();
        request.setObjectiveId(dto.getId());
        request.setPageNumber(Integer.valueOf(1));
        request.setPageSize(Integer.valueOf(0));
        List<ObjectiveKeyResultDTO> keyresutls = this.listKeyResultsForObjective(request).getList();
        if (!CollectionUtils.isEmpty(keyresutls)) {
            keyresutls.forEach(kr -> {
                combineKeyResultPrivilege(kr, user);
            });
            dto.setKeyResults(keyresutls);
        } else {
            dto.setKeyResults(new ArrayList<>());
        }

    }

    private void combineKeyResultForObjective(ObjectiveTreeNodeDTO dto) {
        ListObjectiveKeyResultPageRequestDTO request = new ListObjectiveKeyResultPageRequestDTO();
        request.setObjectiveId(dto.getId());
        request.setPageNumber(Integer.valueOf(1));
        request.setPageSize(Integer.valueOf(0));
        List<ObjectiveKeyResultDTO> keyresutls = this.listKeyResultsForObjective(request).getList();
        if (CollectionUtils.isEmpty(keyresutls)) {
            dto.setKeyResults(new ArrayList<>());
        } else {
            dto.setKeyResults(keyresutls);
        }

    }


    // entity transform
    private List<ObjectiveDTO> batchTransEntity2DTO(List<Objective> list) {
        List<ObjectiveDTO> objectiveDTOS = BeanConvertUtils.batchTransform(ObjectiveDTO.class, list);
        UserInfoDTO currentUser = getCurrentUser();
        for (ObjectiveDTO objectiveDTO : objectiveDTOS) {
            combineKeyResultForObjective(objectiveDTO, currentUser);
        }
        return objectiveDTOS;
    }

    private List<ObjectiveTreeNodeDTO> batchTransEntity2TreeDTO(List<Objective> list) {
        List<ObjectiveTreeNodeDTO> objectiveDTOS = BeanConvertUtils.batchTransform(ObjectiveTreeNodeDTO.class, list);
        return objectiveDTOS.parallelStream().map(objectiveDTO -> {
            combineKeyResultForObjective(objectiveDTO);
            return objectiveDTO;
        }).collect(Collectors.toList());

    }

    private List<ObjectiveConbineWeeklyPlanDTO> batchTranaDTO2ObjectiveConbineWeeklyPlanDTO(List<ObjectiveDTO> list) {
        return BeanConvertUtils.batchTransform(ObjectiveConbineWeeklyPlanDTO.class, list);
    }


    private List<ObjectiveTreeNodeDTO> buildTree(List<ObjectiveTreeNodeDTO> rawList) {
        Set<ObjectiveTreeNodeDTO> brokenRoot = brokenCircle(rawList);
        Set<ObjectiveTreeNodeDTO> root = findTreeRoot(rawList);
        root.addAll(brokenRoot);
        List<ObjectiveTreeNodeDTO> tree = new ArrayList<>(root);
        for (ObjectiveTreeNodeDTO node : rawList) {
            for (ObjectiveTreeNodeDTO child : rawList) {
                if (!root.contains(child) && node.getId().equals(child.getLinkedObjectiveId())) {
                    if (node.getChildren() == null) {
                        node.setChildren(new ArrayList<>());
                    }
                    node.getChildren().add(child);
                }
            }
        }
        return tree;
    }

    private Set<ObjectiveTreeNodeDTO> findTreeRoot(List<ObjectiveTreeNodeDTO> rawList) {
        Set<ObjectiveTreeNodeDTO> heads = new HashSet<>();
        //构建id和node的映射
        Map<Long, ObjectiveTreeNodeDTO> map = new HashMap<>();
        rawList.forEach(node -> {
            map.put(node.getId(), node);
        });

        for (ObjectiveTreeNodeDTO node : rawList) {
            if (map.get(node.getLinkedObjectiveId()) == null) {
                heads.add(node);
            }
        }
        return heads;
    }

    private Set<ObjectiveTreeNodeDTO> brokenCircle(List<ObjectiveTreeNodeDTO> rawList) {
        Set<ObjectiveTreeNodeDTO> heads = new HashSet<>();
        //构建id和node的映射
        Map<Long, ObjectiveTreeNodeDTO> map = new HashMap<>();
        rawList.forEach(node -> {
            map.put(node.getId(), node);
        });
        //遍历判断是否存在圆
        Set<ObjectiveTreeNodeDTO> finded = new HashSet<>();
        for (ObjectiveTreeNodeDTO node : rawList) {
            //没有关联
            if (node.getLinkedObjectiveId() == null) {
                continue;
            }
            //已经在一个环内的
            if (finded.contains(node)) {
                continue;
            }
            //查找环，如果存在将环中最小的id作为树根
            ObjectiveTreeNodeDTO cursor = node;
            Long minId = cursor.getId();
            Set<ObjectiveTreeNodeDTO> circle = new HashSet<>();
            while (cursor != null) {
                if (!circle.contains(cursor)) {
                    if (minId > cursor.getId()) {
                        minId = cursor.getId();
                    }
                    circle.add(cursor);
                    finded.add(cursor);
                    cursor = map.get(cursor.getLinkedObjectiveId());
                } else {
                    //存在环
                    heads.add(map.get(minId));
                    break;
                }
            }
        }
        return heads;
    }


    /*=========================================Objective system notifycation ===================================================*/

    private void sendObjectiveUpdateNotify(Objective objective) {
        notifyManager.sendNotify(new Notify<ObjectiveUpdateSystemNotifyDTO>() {
            @Override
            public NotifyTypeEnum type() {
                return NotifyTypeEnum.SYSTEM;
            }

            @Override
            public ObjectiveUpdateSystemNotifyDTO message() {
                return new ObjectiveUpdateSystemNotifyDTO(objective.getName(), objective.getOwnerName(), objective.getOwnerId(), objective.getId());
            }

            @Override
            public NotifyBusinessType businessType() {
                return NotifyBusinessType.SYSTEM_OBJECTIVE_UPDATE;
            }
        });

    }

    private void sendRemindToUpdateOffTrackObjective(Objective objective) {
        notifyManager.sendNotify(new Notify<ObjectiveOffTraceSystemNotifyDTO>() {
            @Override
            public NotifyTypeEnum type() {
                return NotifyTypeEnum.SYSTEM;
            }

            @Override
            public ObjectiveOffTraceSystemNotifyDTO message() {
                return new ObjectiveOffTraceSystemNotifyDTO(objective.getName(), objective.getOwnerName(), objective.getOwnerId(), objective.getId());
            }

            @Override
            public NotifyBusinessType businessType() {
                return NotifyBusinessType.SYSTEM_OBJECTIVE_OFF_TRACE;
            }
        });
    }



    /*=========================================Objective operate log=============================================*/



    /**
     * insert objective operator time line
     */
    private void insertLog(Long objectiveId, Long organizationId, BussinessLogOperateTypeEnum operateTypeEnum, String desc) {
        this.insertLog(objectiveId, organizationId, BussinessLogEntityEnum.OBJECTIVE, operateTypeEnum, desc);
    }

    /**
     * list objective operator time line
     */
    private PageInfo<ObjectiveTimelineDTO> listLog(Long ObjectiveId, Integer pageNumber, Integer pageSize) {
        GetPagedBussinessLogDTO query = new GetPagedBussinessLogDTO();
        query.setEntityType(BussinessLogEntityEnum.OBJECTIVE);
        query.setRefEntityId(ObjectiveId);
        query.setPageNumber(pageNumber);
        query.setPageSize(pageSize);
        PageInfo<BussinessLogDTO> businessLogList = listLog(query);
        PageInfo<ObjectiveTimelineDTO> objectiveTimelineList = new PageInfo<>();
        BeanConvertUtils.copyEntityProperties(businessLogList, objectiveTimelineList);
        objectiveTimelineList.setList(BeanConvertUtils.batchTransform(ObjectiveTimelineDTO.class, businessLogList.getList()));
        return objectiveTimelineList;
    }
    
    @Autowired
    private ObjectiveDAO objectiveDAO;

    @Autowired
    private KeyResultDAO keyResultDAO;

    @Autowired
    private KeyResultService keyResultService;

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private KeyResultCommentDAO keyResultCommentDAO;

    @Autowired
    private ObjectiveDalilyProgressDAO objectiveDalilyProgressDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private NotifyManager notifyManager;

    @Autowired
    private WeeklyPlanService weeklyPlanService;

}
