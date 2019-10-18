package com.coreteam.okr.service;

import com.coreteam.okr.constant.PrivilegeTypeEnum;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveStatisticsDTO;
import com.coreteam.okr.dto.organization.*;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author jianyong.jiang
 * @date 2019/03/18
 */
public interface OrganizationService {
    /**
     * 添加组织，将当前用户设置为owner，将owner添加为成员，如果存在invited成员，添加邀请记录，并且添加成员
     * @param insertOrganizationDTO
     * @return
     */
    long insertOrganization(InsertOrganizationDTO insertOrganizationDTO);

    /**
     * 更新组织基本信息
     * @param updateOrganizationDTO
     * @return
     */
    boolean updateOrganization(UpdateOrganizationDTO updateOrganizationDTO);


    boolean deleteOrganization(long organizationId);


    OrganizationDTO getOrganizationById(long organizationId);


    /**
     * 获取用户的组织信息
     * @return
     */
    List<OrganizationDTO> listOrganizationsWithDefaultSettings();
    /**
     * list 用户组织信息
     * @return
     */
    List<OrganizationDTO> listOrganizations(Long userId);
    /**
     * list 用户组织综合信息,Objective的数量，member数量
     * @return
     */
    PageInfo<OrganizationComprehensiveDTO> listComprehensiveOrganizations(Long userId, Integer pageNumber, Integer pageSize);

    /**
     * 获取organization的okr
     * @param organizationId
     * @param pageNumber
     * @param pageSize
     * @param start
     * @param end
     * @return
     */
    PageInfo<ObjectiveDTO> listOrganizationLevelObjectives(Long organizationId, Integer pageNumber, Integer pageSize, Date start, Date end);

    /**
     * 获取org的okr统计信息
     * @param organizationId
     * @param start
     * @param end
     * @return
     */
    ObjectiveStatisticsDTO getOrganizationLevelObjectiveStatistics(Long organizationId, Date start, Date end);


    /**
     * 判断用户是否需要创建组织引导
     * @param user
     * @return
     */
    Boolean needOrgnizationCreateGuide(UserInfoDTO user);

    /**
     * 忽略组织创建引导
     * @param user
     */
    void ignoreOrgnizationCreateGuide(UserInfoDTO user);

    /**
     * 设置用户默认的组织
     * @param id
     * @param dto
     */
    void setUserDefaultOrganization(Long id, DefaultOrgnazationSettingDTO dto);
    /**
     *
     * @param organizationId
     * @param type
     * @return
     */
    Boolean hasPermission(Long organizationId, PrivilegeTypeEnum type);

}
