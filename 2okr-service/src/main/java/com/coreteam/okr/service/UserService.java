package com.coreteam.okr.service;

import com.coreteam.okr.dto.Notify.NotificationDTO;
import com.coreteam.okr.dto.member.MemberSimpleInfoDTO;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.dto.organization.DefaultOrgnazationSettingDTO;
import com.coreteam.okr.dto.organization.OrganizationComprehensiveDTO;
import com.coreteam.okr.dto.organization.OrganizationDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanDTO;
import com.coreteam.okr.dto.plan.WeekplayReportTranFormDTO;
import com.coreteam.okr.dto.user.*;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;


/**
 * @ClassName: UserService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 17:57
 * @Version 1.0.0
 */
public interface UserService {

    /*===================================用户基本信息操作===========================================*/

    UserInfoDTO getUserInfoById(Long id);

    UserInfoDTO getUserInfoByEmail(String email);

    UserInfoDTO getCurrentUserInfo();

    void updateUserInfo(UpdateUserInfoDTO userInfoDTO);

    List<MemberSimpleInfoDTO> listPartners();

    String getCurrentUserWechatBindingURL();

    UserWechatInfoDTO getCurrentUserWechatInfo();

    void unbindCurrentUserWechatAccount();

    void bindUserWechatAccount(Long userId,String code);


    /*====================================用户组织信息操作========================================*/

    /**
     * list 用户组织信息
     */
    List<OrganizationDTO> listUserOrganizations();

    /**
     * list 用户组织综合信息,Objective的数量，member数量
     */
    PageInfo<OrganizationComprehensiveDTO> listUserComprehensiveOrganizations(Integer pageNumber, Integer pageSize);

    /**
     * 获取用户是否需要创建组织引导
     */
    Boolean needOrgnizationCreateGuide();

    /**
     * 忽略组织创建引导
     */
    void ignoreOrgnizationCreateGuide();

    /**
     * 设置用户默认的组织
     */
    void setUserDefaultOrganization(DefaultOrgnazationSettingDTO dto);



    /*====================================用户okr信息操作========================================*/

    /**
     * 按照level分组获取用户组织objective的统计信息和详细列表
     */
    ObjectiveStaticAndListGroupByLevelDTO analysisObjectiveStatisticsAndListGroupByLevel(ObjectiveStaticAndListQueryDTO query);

    /**
     * 获取组织所有的Objective，转化成tree
     */
    List<ObjectiveTreeNodeDTO> listObjectiveTree(ObjectiveStaticAndListQueryDTO query);

    /**
     * 获取可供连接的Objective列表
     */
    List<ObjectiveDTO> listObjectivesAble(Long organizationId);

    /**
     * 获取可供连接的Objective tree
     */
    List<ObjectiveTreeNodeDTO> listObjectivesTreeAble(Long organizationId);

    /**
     * 分析Member级别的okr统计信息
     */
    ObjectiveStatisticsDTO getMemberLevelObjectivesStatics(Long organizationId, Date start, Date end);

    /**
     * list user okrs
     */
    PageInfo<ObjectiveDTO> listMemberLevelObjectives(Long organizationId, Integer pageNumber, Integer pageSize, Date start, Date end);




    /*====================================用户weekly plan信息操作========================================*/

    /**
     * 获取用户的周计划
     */
    WeeklyPlanDTO getUserWeeklyPlan(Integer week, Long orgId);

    /**
     * 发送个人周计划
     */
    void sendOrgWeeklyPlanReport(Long id, Integer week, List<String> emails);

    /**
     * 获取成员的weekly plan report
     */
    WeekplayReportTranFormDTO listMyMemberWeeklyPlan(Long id, Integer week);

    /**
     * 获取用户weekly plan report的常规设置
     */
    List<SimpleUserInfoDTO> listUsualWeeklyPlanReportReciver(Long organizationId);

    /**
     * 设置weekly plan report 常规接收人
     */
    void setUsualWeeklyPlanReportReciver(Long organizationId, List<Long> reciverId);

    /**
     * 设置用户个人weekly plan的title
     */
    void setPersonalWeeklyPlanTitle(Long organizationId, String title);

    /**
     * 获取用户个人weeklyplan的title的设置
     */
    String getPersonalWeeklyPlanTitle(Long organizationId);


    /*====================================用户站内消息操作========================================*/

    /**
     * 统计用户通知数量
     */
    Integer countNotifycations();

    /**
     * 获取用户通知列表
     */
    List<NotificationDTO> listNotifycations();

    /**
     * 将用户所有的未读消息标记为已经读取
     */
    void markAllNotificationRead();


}
