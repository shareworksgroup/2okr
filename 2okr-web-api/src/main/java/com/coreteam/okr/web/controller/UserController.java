package com.coreteam.okr.web.controller;

import com.coreteam.okr.aop.SubscriptionActiveLimit;
import com.coreteam.okr.dto.Notify.NotificationDTO;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.dto.organization.DefaultOrgnazationSettingDTO;
import com.coreteam.okr.dto.organization.OrganizationDTO;
import com.coreteam.okr.dto.plan.*;
import com.coreteam.okr.dto.user.SimpleUserInfoDTO;
import com.coreteam.okr.dto.user.UpdateUserInfoDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.dto.user.UserWechatInfoDTO;
import com.coreteam.okr.service.MemberService;
import com.coreteam.okr.service.OkrService;
import com.coreteam.okr.service.UserService;
import com.coreteam.okr.service.WeeklyPlanService;
import com.coreteam.okr.util.WeeklyPlanViewRender;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 8:19
 * @Version 1.0.0
 */

@RestController
@RequestMapping("users")
@AuditLogAnnotation(value = "users接口")
@Slf4j
public class UserController {

    /*========================================用户base info 操作================================================*/

    @GetMapping
    @ApiOperation("获取当前的用户信息详情")
    public UserInfoDTO getUserInfo() {
        UserInfoDTO dto = userService.getUserInfoById(userService.getCurrentUserInfo().getId());
        return dto == null ? new UserInfoDTO() : dto;
    }

    @PutMapping()
    @ApiOperation("更新当前用户基本信息")
    public void updateUserInfo(@RequestBody @Valid UpdateUserInfoDTO userInfoDTO) {
        userService.updateUserInfo(userInfoDTO);
    }

    @GetMapping("/social/wechat")
    @ApiOperation("获取当前的用户微信绑定信息")
    public UserWechatInfoDTO getUserWechatInfo(){
        return this.userService.getCurrentUserWechatInfo();
    }

    @GetMapping("/social/wechat/url")
    @ApiOperation("获取微信绑定的url")
    public String getBindWechatAccountUrl(){
        return this.userService.getCurrentUserWechatBindingURL();
    }

    @DeleteMapping("/social/wechat")
    @ApiOperation("解除微信绑定账号")
    public void unBindWechatAccount(){
        this.userService.unbindCurrentUserWechatAccount();
    }

    @GetMapping("{id}/social/wechat/binde/callback")
    @ApiOperation("维系账号绑定回调")
    public void bindWechatAccount(@PathVariable("id") Long userId,@RequestParam("code") String code){
         this.userService.bindUserWechatAccount(userId,code);
    }




    /*========================================用户组织资源查询================================================*/

    @GetMapping("/organization")
    @ApiOperation("获取当前用户的组织信息")
    public List<OrganizationDTO> listUserOrganization() {
        return userService.listUserOrganizations();
    }


    /*==================用户个人配置(默认组织，weekly plan reciver ，weekly plan title)========================*/
    @GetMapping("/organization/guide")
    @ApiOperation("获取当前用户是否需要创建组织引导")
    public Boolean needOrgnizationCreateGuide() {
        return userService.needOrgnizationCreateGuide();
    }

    @PostMapping("/organization/{id}/default")
    @ApiOperation("设置用户的默认公司")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public void setUserDefaultOrganization(@PathVariable("id") @NotNull Long organizationId) {
        DefaultOrgnazationSettingDTO dto = new DefaultOrgnazationSettingDTO();
        dto.setOrganizationId(organizationId);
        userService.setUserDefaultOrganization(dto);
    }

    @PostMapping("/organization/guide/ignore")
    @ApiOperation("用户忽略组织创建引导")
    public void ignoreOrgnizationCreateGuide() {
        userService.ignoreOrgnizationCreateGuide();
    }

    @GetMapping("/organization/{id}/weeklyplan_report_reciver")
    @ApiOperation("获取用户weekly plan report 接受人")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public List<SimpleUserInfoDTO> listUsualWeeklyPlanReportReciver(@PathVariable("id") @NotNull Long organizationId) {
        return userService.listUsualWeeklyPlanReportReciver(organizationId);
    }

    @PostMapping("/organization/{id}/weeklyplan_report_reciver")
    @ApiOperation("设置用户weekly plan report 接受人")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public void setUsualWeeklyPlanReportReciver(@PathVariable("id") @NotNull Long organizationId, @RequestBody @Valid WeeklyPlanReportReciverSettingDTO settings) {
        userService.setUsualWeeklyPlanReportReciver(organizationId, settings.getReciverId());
    }


    @PostMapping("/organization/{id}/personal_weeklyplan_title")
    @ApiOperation("设置用户个人weeklyplan的title")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public void setPersonalWeeklyPlanTitle(@PathVariable("id") @NotNull Long organizationId, @RequestBody @Valid PersonalWeeklyPlanSettingDTO settings) {
        userService.setPersonalWeeklyPlanTitle(organizationId, settings.getTitle());
    }

    @GetMapping("/organization/{id}/personal_weeklyplan_title")
    @ApiOperation("获取用户个人weeklyplan的title")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public String getPersonalWeeklyPlanTitle(@PathVariable("id") @NotNull Long organizationId) {
        return userService.getPersonalWeeklyPlanTitle(organizationId);
    }

    /*=================================查询某组织中的OKR资源=====================================*/

    //分析统计组织所有级别的okr
    @GetMapping("/organization/okr/statistics_list/group_by_level")
    @ApiOperation("获取user objective report")
    @SubscriptionActiveLimit(organizationId = "#{query.organizationId}")
    public ObjectiveStaticAndListGroupByLevelDTO analysisObjectiveStatisticsAndListGroupByLevel(ObjectiveStaticAndListQueryDTO query) {
        return userService.analysisObjectiveStatisticsAndListGroupByLevel(query);
    }

    //list组织所有级别的okr,构建tree
    @GetMapping("/organization/okr/tree")
    @ApiOperation("获取user objective tree")
    @SubscriptionActiveLimit(organizationId = "#{query.organizationId}")
    public List<ObjectiveTreeNodeDTO> listObjectiveTreeView(ObjectiveStaticAndListQueryDTO query) {
        return userService.listObjectiveTree(query);
    }

    @GetMapping("/organization/{id}/okr/linkable")
    @ApiOperation("获取user可供链接的objective列表")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public List<ObjectiveDTO> listOrgObjectiveLinkable(@PathVariable("id") @NotNull Long organizationId) {
        return this.userService.listObjectivesAble(organizationId);
    }

    @GetMapping("/organization/{id}/okr/linkable/tree")
    @ApiOperation("获取user可供链接的objective列表")
    @Deprecated
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public List<ObjectiveTreeNodeDTO> listOrgObjectiveTreeLinkable(@PathVariable("id") @NotNull Long organizationId) {
        return this.userService.listObjectivesTreeAble(organizationId);
    }


    //分页获取当前组织，Member级别的okr
    @GetMapping("/organization/{id}/okr")
    @ApiOperation("获取user拥有的objective列表")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public PageInfo<ObjectiveDTO> listMemberLevelObjectiveKeyResult(@PathVariable("id") @NotNull Long organizationId, Integer pageNumber, Integer pageSize, Date start, Date end) {
        return this.userService.listMemberLevelObjectives(organizationId, pageNumber, pageSize, start, end);
    }

    //统计当前组织，Member级别的okr
    @GetMapping("/organization/{id}/okr/statistics")
    @ApiOperation("获取user拥有的objective的统计信息")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public ObjectiveStatisticsDTO getMemberLevelObjectiveStatistics(@PathVariable("id") @NotNull Long organizationId, Date start, Date end) {
        return this.userService.getMemberLevelObjectivesStatics(organizationId, start, end);
    }


    /*=================================查询某组织中的weekly plan 资源=====================================*/


    @GetMapping("/organization/{id}/weeklyplan/category_type")
    @ApiOperation("获取用户指定week的周计划，按照类别展示，如果没有指定week默认获取本周的计划，week格式为201915")
    @SubscriptionActiveLimit(organizationId = "#{id}")
    public WeeklyPlanCategoryDTO listWeeklyPlanCategories(@PathVariable("id") @NotNull Long id, @RequestParam(required = false) Integer week) {
        return WeeklyPlanViewRender.transformCategoryView(userService.getUserWeeklyPlan(week, id));
    }

    @GetMapping("/organization/{id}/weeklyplan/objective_type")
    @ApiOperation("获取用户指定week的周计划，按照类别展示，如果没有指定week默认获取本周的计划，week格式为201915")
    @SubscriptionActiveLimit(organizationId = "#{id}")
    public WeeklyPlanObjectiveDTO listWeeklyPlanObjective(@PathVariable("id") @NotNull Long id, @RequestParam(required = false) Integer week) {
        return WeeklyPlanViewRender.transformObjectiveView(userService.getUserWeeklyPlan(week, id));
    }


    @GetMapping("/organization/{id}/weeklyplan/report/user_type")
    @ApiOperation("获取组织的weeklyplan 报告，按照user的形式展示")
    @SubscriptionActiveLimit(organizationId = "#{id}")
    public Map<String, List<WeeklyPlanReportUserDTO>> listOrgWeeklyPlanReportByUser(@PathVariable("id") @NotNull Long id, @RequestParam(required = false) Integer week) {
        return WeeklyPlanViewRender.transformReportByUserView(userService.listMyMemberWeeklyPlan(id, week), this.memberService.listOrganizationTeamMembers(id));
    }

    @GetMapping("/organization/{id}/weeklyplan/report/categories_type")
    @ApiOperation("获取组织的weeklyplan 报告，按照categories的形式展示")
    @SubscriptionActiveLimit(organizationId = "#{id}")
    public WeeklyPlanReportCategoryDTO listOrgWeeklyPlanReportByCategory(@PathVariable("id") @NotNull Long id, @RequestParam(required = false) Integer week) {
        return WeeklyPlanViewRender.transformReportByCategory(this.userService.listMyMemberWeeklyPlan(id, week));
    }

    @GetMapping("/organization/{id}/weeklyplan/report/objective_type")
    @ApiOperation("获取组织的weeklyplan 报告，按照objective的形式展示")
    @SubscriptionActiveLimit(organizationId = "#{id}")
    public WeeklyPlanReportObjectiveTreeDTO listOrgWeeklyPlanReportByObjective(@PathVariable("id") @NotNull Long id, @RequestParam(required = false) Integer week) {
        return WeeklyPlanViewRender.transformReportByObjectiveTree(this.userService.listMyMemberWeeklyPlan(id, week));
    }

    @GetMapping("/organization/{id}/weeklyplan/report/summary")
    @ApiOperation("获取组织的weeklyplan 报告summary信息")
    @SubscriptionActiveLimit(organizationId = "#{id}")
    public List<WeeklyPlanReportSummaryDTO> listOrgWeeklyPlanReportSummary(@PathVariable("id") @NotNull Long id, @RequestParam(required = false) Integer week) {
        return this.weeklyPlanService.listOrgWeeklyPlanReportSummary(id, week);
    }

    @PostMapping("/organization/{id}/weeklyplan/send/")
    @ApiOperation("发送组织的weeklyPlan给当前用户")
    @SubscriptionActiveLimit(organizationId = "#{id}")
    public void sendOrgWeeklyPlanReport(@PathVariable("id") @NotNull Long id, @RequestBody @Valid WeeklyDTO weeklyDTO) {
        userService.sendOrgWeeklyPlanReport(id, weeklyDTO.getWeek(), weeklyDTO.getExternalEmails());
    }

    /*=================================操作用户站内消息提醒资源=====================================*/

    @GetMapping("/notification/count")
    @ApiOperation("获取当前用户站内提醒的数量")
    public Integer countUserNotifications() {
        return userService.countNotifycations();
    }

    @GetMapping("/notification")
    @ApiOperation("获取当前用户站内提醒列表")
    public List<NotificationDTO> listUserNotifications() {
        return userService.listNotifycations();
    }


    @Autowired
    private UserService userService;

    @Autowired
    private WeeklyPlanService weeklyPlanService;

    @Autowired
    private OkrService okrService;

    @Autowired
    private MemberService memberService;



}
