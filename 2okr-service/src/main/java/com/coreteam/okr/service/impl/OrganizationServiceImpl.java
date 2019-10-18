package com.coreteam.okr.service.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.Authorization.OrganizationPermissionRulesParser;
import com.coreteam.okr.constant.*;
import com.coreteam.okr.dao.MemberSettingDAO;
import com.coreteam.okr.dao.OrganizationDAO;
import com.coreteam.okr.dto.Authorization.OrganizationPrivilege;
import com.coreteam.okr.dto.member.InsertMemberDTO;
import com.coreteam.okr.dto.member.InsertMemberNeedInviteDTO;
import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.objective.CountObjectiveByStatusDTO;
import com.coreteam.okr.dto.objective.ListObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveStatisticsDTO;
import com.coreteam.okr.dto.organization.*;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.MemberSetting;
import com.coreteam.okr.entity.Organization;
import com.coreteam.okr.manager.BasicMemberManager;
import com.coreteam.okr.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @ClassName: OrganizationServiceImpl
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/26 14:42
 * @Version 1.0.0
 */
@Service
public class OrganizationServiceImpl extends BaseService implements OrganizationService {

    @Autowired
    private OrganizationPermissionRulesParser organizationPermissionRulesParser;


    @Override
    public List<OrganizationDTO> listOrganizationsWithDefaultSettings() {
        UserInfoDTO curreentUser = getCurrentUser();
        List<OrganizationDTO> list= listOrganizations(curreentUser.getId());
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return combineOrganizationDefaultSettings(curreentUser, list);
    }

    @Override
    public List<OrganizationDTO> listOrganizations(Long userId) {
        List<OrganizationDTO> orgList = BeanConvertUtils.batchTransform(OrganizationDTO.class, this.dao.listOrganizationsByUser(userId));
        combinePrivilege(orgList);
        return orgList;
    }

    @Override
    public PageInfo<OrganizationComprehensiveDTO> listComprehensiveOrganizations(Long userId, Integer pageNumber, Integer pageSize) {
        if (pageNumber == null || pageSize == null) {
            throw new CustomerException("page size and page num must not be null ");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Organization> orgs = this.dao.listOrganizationsByUser(userId);
        PageInfo pageInfo = new PageInfo(orgs);
        List<OrganizationComprehensiveDTO> list = new ArrayList<>();
        for (Organization organization : orgs) {
            OrganizationComprehensiveDTO dto = new OrganizationComprehensiveDTO();
            BeanConvertUtils.copyEntityProperties(organization, dto);
            dto.setOkrNum(okrService.sumOrganizationObjective(organization.getId()));
            dto.setMemberNum(memberService.countOrganizationMember(organization.getId()));
            dto.setOwner(organization.getManagerName());
            list.add(dto);
        }
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public PageInfo<ObjectiveDTO> listOrganizationLevelObjectives(Long organizationId, Integer pageNumber, Integer pageSize, Date start, Date end) {
        ListObjectiveDTO queryDTO = new ListObjectiveDTO();
        queryDTO.setBusinessId(organizationId);
        queryDTO.setPageNumber(pageNumber);
        queryDTO.setPageSize(pageSize);
        queryDTO.setStart(start);
        queryDTO.setEnd(end);
        queryDTO.setLevel(ObjectiveLevelEnum.ORGANIZATION.name());
        queryDTO.setOrganizationId(organizationId);
        return okrService.listObjectiveByLevelFilter(queryDTO);
    }

    @Override
    public ObjectiveStatisticsDTO getOrganizationLevelObjectiveStatistics(Long organizationId, Date start, Date end) {
        CountObjectiveByStatusDTO query = new CountObjectiveByStatusDTO();
        query.setBusinessId(organizationId);
        query.setStart(start);
        query.setEnd(end);
        query.setLevel(ObjectiveLevelEnum.ORGANIZATION.name());
        query.setOrganizationId(organizationId);
        ObjectiveStatisticsDTO dto = okrService.countObjectiveNumByStatusAndListOverTimeDataByLevelFilter(query);
        if (dto == null) {
            dto = new ObjectiveStatisticsDTO();
        }
        return okrService.countObjectiveNumByStatusAndListOverTimeDataByLevelFilter(query);
    }

    @Override
    @Transactional
    public long insertOrganization(InsertOrganizationDTO insertOrganizationDTO) {
        UserInfoDTO user = getCurrentUser();

        //创建组织
        Organization organization = new Organization();
        BeanUtils.copyProperties(insertOrganizationDTO, organization);
        organization.initializeForInsert();
        organization.setManagerId(user.getId());
        organization.setManagerName(user.getName());
        organization.setCreatedUser(user.getId());
        organization.setCreatedName(user.getName());
        this.dao.insert(organization);

        //设置用户默认组织
        MemberSetting memberSetting = this.memberSettingService.getDefaultOrganization(user.getId());
        if (memberSetting == null) {
            this.memberSettingService.setDefaultOrganization(user.getId(), organization.getId());
        }

        //设置默认的weekly plan 模板
        this.templateService.setOrganizationDefaultWeeklyPlanTemplate(organization);

        //添加成员
        InsertMemberDTO memberDTO = new InsertMemberDTO(organization.getId(), null, user.getId(), user.getName(), MemberTypeEnum.ORGANIZATION, null);
        this.memberService.insertMember(memberDTO, true, true);
        if (!CollectionUtils.isEmpty(insertOrganizationDTO.getInvited())) {
            Set<String> invitedEmails = new HashSet<>(insertOrganizationDTO.getInvited());
            for (String email : invitedEmails) {
                if (email.equals(user.getEmail())) {
                    continue;
                }
                InsertMemberNeedInviteDTO dto = new InsertMemberNeedInviteDTO(organization.getId(), null, email, MemberTypeEnum.ORGANIZATION);
                this.memberService.insertMember(dto, false, true);
            }
        }
        String logDesc = String.format("%s created organization %s.", user.getName(), organization.getName());
        this.insertLog(organization.getId(), BussinessLogOperateTypeEnum.INSERT, logDesc);
        return organization.getId();
    }


    @Override
    @Transactional
    public boolean updateOrganization(UpdateOrganizationDTO updateOrganizationDTO) {
        if (!hasPermission(updateOrganizationDTO.getId(), PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException("has no permission to operate the organization");
        }
        Organization organization = new Organization();
        BeanUtils.copyProperties(updateOrganizationDTO, organization);
        organization.initializeForUpdate();

        int result = this.dao.updateByPrimaryKeySelective(organization);
        if (result > 0) {
            String logDesc = String.format("%s updated organization %s.", getCurrentUser().getName(), organization.getName());
            this.insertLog(organization.getId(), BussinessLogOperateTypeEnum.UPDATE, logDesc);
        }
        return result > 0;
    }

    @Transactional
    @Override
    public boolean deleteOrganization(long organizationId) {
        if (!hasPermission(organizationId, PrivilegeTypeEnum.DELETABLE)) {
            throw new CustomerException("has no permission to operate the organization");
        }
        int result = this.dao.deleteByPrimaryKey(organizationId);

        if (result > 0) {
            String logDesc = String.format("%s deleted organization, id is: %s.", this.getCurrentUser().getName(), organizationId);
            this.insertLog(organizationId, BussinessLogOperateTypeEnum.DELETE, logDesc);
        }
        return result > 0;
    }

    @Override
    public OrganizationDTO getOrganizationById(long organizationId) {
        Organization organization = this.dao.selectByPrimaryKey(organizationId);
        if (organization == null) {
            return new OrganizationDTO();
        } else {
            OrganizationDTO dto = new OrganizationDTO();
            BeanUtils.copyProperties(organization, dto);
            combinePrivilege(dto);
            return dto;
        }
    }

    @Override
    public Boolean hasPermission(Long organizationId, PrivilegeTypeEnum type) {
        UserInfoDTO currentUser = getCurrentUser();
        List<MemberDTO> orgOwner = this.memberService.listOrganizationMembers(organizationId, true);
        OrganizationPrivilege privilege = organizationPermissionRulesParser.getUserPrivilege(currentUser, orgOwner);
        switch (type) {
            case TEAM_ADDABLE:
                return privilege.getTeamAddable();
            case UPDATABLE:
                return privilege.getUpdatable();
            case VIEWABLE:
                return privilege.getViewable();
            case DELETABLE:
                return privilege.getDeletable();
            case ADDABLE:
                return privilege.getAddable();
        }
        return false;
    }

    private void combinePrivilege(List<OrganizationDTO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            UserInfoDTO currentUser = getCurrentUser();
            list.forEach(org -> {
                org.setPrivilege(organizationPermissionRulesParser.getUserPrivilege(currentUser,  this.memberService.listOrganizationMembers(org.getId(), true)));
            });
        }
    }

    private void combinePrivilege(OrganizationDTO org) {
        UserInfoDTO currentUser = getCurrentUser();
        org.setPrivilege(organizationPermissionRulesParser.getUserPrivilege(currentUser, this.memberService.listOrganizationMembers(org.getId(), true)));
    }


    private List<OrganizationDTO> combineOrganizationDefaultSettings(UserInfoDTO curreentUser, List<OrganizationDTO> list) {
        //绑定默认的组织
        MemberSetting setting = this.memberSettingService.getDefaultOrganization(curreentUser.getId());
        if(!CollectionUtils.isEmpty(list)&&setting!=null){
            list.forEach(organizationDTO -> {
                if(organizationDTO.getId().equals(Long.valueOf(setting.getValue()))){
                    organizationDTO.setIsDefault(true);
                }else{
                    organizationDTO.setIsDefault(false);
                }
            });
        }
        return list;
    }


    @Override
    public Boolean needOrgnizationCreateGuide(UserInfoDTO user) {
        MemberSetting setting = memberSettingService.getOrganizationCreateGuide(user.getId());
        if (setting != null) {
            return false;
        }
        return listOrganizations(user.getId()).size() <= 0;
    }

    @Override
    public void ignoreOrgnizationCreateGuide(UserInfoDTO user) {
        memberSettingService.setOrganizationCreateGuidIgnore(user.getId());
    }

    @Override
    public void setUserDefaultOrganization(Long id, DefaultOrgnazationSettingDTO dto) {
        checkOrgExsit(dto.getOrganizationId());
        setDefaultOrganization(id, dto.getOrganizationId());
    }

    private void setDefaultOrganization(Long userId, Long orgId) {
        this.memberSettingService.setDefaultOrganization(userId, orgId);
    }

    private Organization checkOrgExsit(Long orgId) {
        Organization org = this.dao.selectByPrimaryKey(orgId);
        if (org == null) {
            throw new CustomerException("organization not exsit");
        }
        return org;
    }


    private void insertLog(Long organizationId, BussinessLogOperateTypeEnum operateTypeEnum, String desc) {
        this.insertLog(organizationId, organizationId, BussinessLogEntityEnum.ORGANIZATION, operateTypeEnum, desc);
    }

    @Autowired
    private OrganizationDAO dao;

    @Autowired
    private BasicMemberManager basicMemberManager;

    @Autowired
    private OkrService okrService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private MemberSettingDAO memberSettingDAO;

    @Autowired
    private MemberSettingService memberSettingService;

    @Autowired
    private WeeklyPlanTemplateService templateService;


}
