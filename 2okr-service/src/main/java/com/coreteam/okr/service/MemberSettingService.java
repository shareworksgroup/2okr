package com.coreteam.okr.service;

import com.coreteam.okr.entity.MemberSetting;

import java.util.List;

/**
 * @ClassName: MemberSettingService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/21 16:33
 * @Version 1.0.0
 */
public interface MemberSettingService {
    /**
     * 获取组织创建引导配置
     * @param userId
     * @return
     */
    MemberSetting getOrganizationCreateGuide(Long userId);

    /**
     * 设置忽略组织创建引导
     * @param userId
     */
    void setOrganizationCreateGuidIgnore(Long userId);

    /**
     * 获取用户默认的组织
     * @param userId
     * @return
     */
    MemberSetting getDefaultOrganization(Long userId);

    /**
     * 设置默认的组织id
     * @param userId
     * @param orgId
     */
    void setDefaultOrganization(Long userId, Long orgId);

    MemberSetting getUserThemeSetting(Long id);

    MemberSetting getUserLangSetting(Long id);

    void updateUserThemeSetting(Long id, String value);

   void updateUserLangSetting(Long id, String value);

    /**
     * 设置用户 weekly plan report 常规的接收人
     * @param id
     * @param organizationId
     * @param reciverIds
     */
    void setUsualWeeklyPlanReportReciver(Long id, Long organizationId, List<Long> reciverIds);

    List<Long>  listUsualWeeklyPlanReportReciver(Long organizationId, Long id);

    /**
     * 设置用户个人weekly plan title
     * @param organizationId
     * @param id
     * @param title
     */
    void setPersonalWeeklyPlanTitle(Long organizationId, Long id, String title);

    /**
     * 获取用户个人weekly plan title
     * @param organizationId
     * @param id
     * @return
     */
    MemberSetting getPersonalWeeklyPlanTitle(Long organizationId, Long id);
}
