package com.coreteam.okr.service.impl;

import com.coreteam.okr.client.userserviece.UserServiceClient;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.coreteam.okr.constant.UserLangTypeEnum;
import com.coreteam.okr.constant.UserThemeTypeEnum;
import com.coreteam.okr.dto.Notify.NotificationDTO;
import com.coreteam.okr.dto.member.MemberSimpleInfoDTO;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.dto.organization.DefaultOrgnazationSettingDTO;
import com.coreteam.okr.dto.organization.OrganizationComprehensiveDTO;
import com.coreteam.okr.dto.organization.OrganizationDTO;
import com.coreteam.okr.dto.plan.ListWeeklyPlanDTO;
import com.coreteam.okr.dto.plan.SendOrgWeeklyPlanReportDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanDTO;
import com.coreteam.okr.dto.plan.WeekplayReportTranFormDTO;
import com.coreteam.okr.dto.user.*;
import com.coreteam.okr.entity.MemberSetting;
import com.coreteam.okr.manager.NotifyManager;
import com.coreteam.okr.service.*;
import com.coreteam.web.session.WebSession;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: UserServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 17:59
 * @Version 1.0.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    /*===================================用户基本信息操作===========================================*/

    /**
     * 用户基本信息操作
     */
    @Override
    public UserInfoDTO getUserInfoById(Long id) {
        UserDTO userDTO = null;
        try {
            userDTO = userServiceClient.getUserInfoById(id);
        } catch (Exception e) {
            throw new RuntimeException("failure to get user info by id：" + id + " message:" + e.getMessage());
        }

        UserInfoDTO info = new UserInfoDTO();
        if (userDTO != null) {
            info.setId(userDTO.getId());
            info.setName(userDTO.getUserName());
            info.setEmail(userDTO.getEmail());
            info.setPhone(userDTO.getPhoneNumber());
            //获取用户的theme设置
            MemberSetting themeSetting = memberSettingService.getUserThemeSetting(userDTO.getId());
            //获取用户的language设置
            MemberSetting langSetting = memberSettingService.getUserLangSetting(userDTO.getId());
            if (themeSetting == null) {
                info.setTheme(UserThemeTypeEnum.LIGHT.name());
            } else {
                info.setTheme(themeSetting.getValue());
            }
            if (langSetting == null) {
                info.setLanguage(UserLangTypeEnum.US.name());
            } else {
                info.setLanguage(langSetting.getValue());
            }
        }
        return info;
    }

    @Override
    public UserInfoDTO getUserInfoByEmail(String email) {
        UserInfoDTO user = new UserInfoDTO();
        UserDTO user2 = null;
        try {
            user2 = userServiceClient.getUserByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("failure to get user info by email：" + email + " message:" + e.getMessage());
        }
        if (user2 == null) {
            return user;
        }
        user.setEmail(user2.getEmail());
        user.setId(user2.getId());
        user.setName(user2.getUserName());
        user.setPhone(user2.getPhoneNumber());
        return user;
    }

    @Override
    public UserInfoDTO getCurrentUserInfo() {
        UserInfoDTO info = new UserInfoDTO();
        if (info == null) {
            throw new RuntimeException("failure to get current user info");
        }
        info.setId(webSession.getUserId());
        info.setName(webSession.getUserName());
        info.setEmail(webSession.getEmail());
        return info;
    }

    @Override
    public void updateUserInfo(UpdateUserInfoDTO updateUserInfoDTO) {
        try {
            UserInfoDTO currentUser = getCurrentUserInfo();
            UpdateRemoteUserInfoDTO dto = new UpdateRemoteUserInfoDTO();
            dto.setUserName(updateUserInfoDTO.getName());
            dto.setPhoneNumber(updateUserInfoDTO.getPhone());
            userServiceClient.updateUserInfo(currentUser.getId(), dto);
            // update theme and language;
            if (updateUserInfoDTO.getTheme() != null) {
                memberSettingService.updateUserThemeSetting(currentUser.getId(), updateUserInfoDTO.getTheme().name());
            }
            if (updateUserInfoDTO.getLanguage() != null) {
                memberSettingService.updateUserLangSetting(currentUser.getId(), updateUserInfoDTO.getLanguage().name());
            }

        } catch (Exception e) {
            throw new RuntimeException("failure to update user info ，message：" + e.getMessage());
        }

    }

    @Override
    public List<MemberSimpleInfoDTO> listPartners() {
        UserInfoDTO user = getCurrentUserInfo();
        return this.memberService.listPartnersCorssOrganization(user);
    }

    @Override
    public String getCurrentUserWechatBindingURL() {
        UserInfoDTO currentUser = getCurrentUserInfo();
        return  wxMpService.buildQrConnectUrl(returnUrl, "snsapi_login",String.valueOf(currentUser.getId()));
    }


    @Override
    public UserWechatInfoDTO getCurrentUserWechatInfo() {
        UserInfoDTO currentUser = getCurrentUserInfo();
        UserWechatInfoDTO userWechatInfo = new UserWechatInfoDTO();
        try {
            userWechatInfo = this.userServiceClient.getUserWechatInfoById(currentUser.getId());
        } catch (Exception e) {
            log.error("user-service 获取用户微信信息失败");
            log.error(ExceptionUtil.stackTraceToString(e));
        }

        return userWechatInfo;
    }

    @Override
    public void unbindCurrentUserWechatAccount() {
        UserInfoDTO currentUser = getCurrentUserInfo();
        this.userServiceClient.deleteUserWechatInfo(currentUser.getId());

    }

    @Override
    public void bindUserWechatAccount(Long userId, String code) {
        WxMpOAuth2AccessToken weChatToken = null;
        WxMpUser wxMpUse=null;
        try {
            weChatToken = wxMpService.oauth2getAccessToken(code);
            wxMpUse = wxMpService.oauth2getUserInfo(weChatToken,"zh_CN");
        } catch (Exception e) {
            log.error(ExceptionUtil.stackTraceToString(e));
        }
        if(weChatToken!=null&&wxMpUse!=null){

            UpdateUserWechatInfoDTO userWechatInfoDTO =new UpdateUserWechatInfoDTO();
            userWechatInfoDTO.setUserId(userId);
            userWechatInfoDTO.setAccountName(wxMpUse.getNickname());
            userWechatInfoDTO.setOauthId(weChatToken.getUnionId());
            this.userServiceClient.updateUserWechatInfo(userWechatInfoDTO.getUserId(), userWechatInfoDTO);
        }
    }

    /*====================================用户组织信息操作========================================*/

    @Override
    public List<OrganizationDTO> listUserOrganizations() {
        return organizationService.listOrganizationsWithDefaultSettings();
    }

    @Override
    public PageInfo<OrganizationComprehensiveDTO> listUserComprehensiveOrganizations(Integer pageNumber, Integer pageSize) {
        return organizationService.listComprehensiveOrganizations(getCurrentUserInfo().getId(), pageNumber, pageSize);
    }

    @Override
    public Boolean needOrgnizationCreateGuide() {
        UserInfoDTO user = getCurrentUserInfo();
        return organizationService.needOrgnizationCreateGuide(user);
    }

    @Override
    public void ignoreOrgnizationCreateGuide() {
        UserInfoDTO user = getCurrentUserInfo();
        organizationService.ignoreOrgnizationCreateGuide(user);
    }


    @Override
    public void setUserDefaultOrganization(DefaultOrgnazationSettingDTO dto) {
        UserInfoDTO currentUser = getCurrentUserInfo();
        this.organizationService.setUserDefaultOrganization(currentUser.getId(), dto);
    }

    /*====================================用户okr信息操作========================================*/


    @Override
    public PageInfo<ObjectiveDTO> listMemberLevelObjectives(Long organizationId, Integer pageNumber, Integer pageSize, Date start, Date end) {
        ListObjectiveDTO queryDTO = new ListObjectiveDTO();
        queryDTO.setPageNumber(pageNumber);
        queryDTO.setPageSize(pageSize);
        queryDTO.setStart(start);
        queryDTO.setEnd(end);
        queryDTO.setLevel(ObjectiveLevelEnum.MEMBER.name());
        queryDTO.setBusinessId(getCurrentUserInfo().getId());
        queryDTO.setOrganizationId(organizationId);
        return okrService.listObjectiveByLevelFilter(queryDTO);
    }

    @Override
    public List<ObjectiveTreeNodeDTO> listObjectiveTree(ObjectiveStaticAndListQueryDTO query) {
        if (query.getTeamId() != null || query.getUserId() != null) {
            query.setSearch(null);
        }
        return okrService.listObjectiveTreeView(query);
    }

    @Override
    public List<ObjectiveDTO> listObjectivesAble(Long organizationId) {
        UserInfoDTO user = getCurrentUserInfo();
        return okrService.listObjectiveLinkable(user.getId(), organizationId);
    }

    @Override
    public List<ObjectiveTreeNodeDTO> listObjectivesTreeAble(Long organizationId) {
        UserInfoDTO user = getCurrentUserInfo();
        return okrService.listObjectiveTreeViewLinkable(user.getId(), organizationId);
    }

    @Override
    public ObjectiveStatisticsDTO getMemberLevelObjectivesStatics(Long organizationId, Date start, Date end) {
        CountObjectiveByStatusDTO query = new CountObjectiveByStatusDTO();
        query.setStart(start);
        query.setEnd(end);
        query.setLevel(ObjectiveLevelEnum.MEMBER.name());
        query.setBusinessId(getCurrentUserInfo().getId());
        query.setOrganizationId(organizationId);
        return okrService.countObjectiveNumByStatusAndListOverTimeDataByLevelFilter(query);
    }

    @Override
    public ObjectiveStaticAndListGroupByLevelDTO analysisObjectiveStatisticsAndListGroupByLevel(ObjectiveStaticAndListQueryDTO query) {
        if (query.getTeamId() != null || query.getUserId() != null) {
            query.setSearch(null);
        }
        return okrService.analysisObjectiveStatisticsAndListByLevel(query);
    }

    /*====================================用户weekly plan信息操作========================================*/

    @Override
    public WeeklyPlanDTO getUserWeeklyPlan(Integer week, Long orgId) {
        UserInfoDTO userInfo = getCurrentUserInfo();
        ListWeeklyPlanDTO query = new ListWeeklyPlanDTO();
        query.setUserId(userInfo.getId());
        query.setWeek(week);
        query.setOrganizationId(orgId);
        return weeklyPlanService.listUserWeeklyPlanByOrg(query);
    }

    @Override
    public void sendOrgWeeklyPlanReport(Long id, Integer week, List<String> emails) {
        SendOrgWeeklyPlanReportDTO sendInfo = new SendOrgWeeklyPlanReportDTO();
        Set<String> toSends = new HashSet<>();
        //添加额外需要发送的人
        if (!CollectionUtils.isEmpty(emails)) {
            toSends.addAll(emails);
        }
        sendInfo.setOrgid(id);
        UserInfoDTO userInfo = getCurrentUserInfo();
        sendInfo.setUserid(userInfo.getId());
        sendInfo.setUserName(userInfo.getName());
        //添加自己
        toSends.add(userInfo.getEmail());
        sendInfo.setEmail(toSends);
        sendInfo.setWeek(week);
        weeklyPlanService.sendOrgWeeklyPlanReport(sendInfo);
    }


    @Override
    public WeekplayReportTranFormDTO listMyMemberWeeklyPlan(Long id, Integer week) {
        UserInfoDTO user = getCurrentUserInfo();
        return weeklyPlanService.listOrgWeeklyPlanReport(id, user.getId(), week);
    }

    @Override
    @Transactional
    public void setUsualWeeklyPlanReportReciver(Long organizationId, List<Long> reciverIds) {
        UserInfoDTO currentUser = getCurrentUserInfo();
        this.memberSettingService.setUsualWeeklyPlanReportReciver(currentUser.getId(), organizationId, reciverIds);
        //在完成用户reciver设置后，变更本周weekly plan的邮件接受人。
        this.weeklyPlanService.autoSendThisWeeklyPlanReportToLeader(organizationId, currentUser.getId(), reciverIds);
    }

    @Override
    public void setPersonalWeeklyPlanTitle(Long organizationId, String title) {
        UserInfoDTO currentUser = getCurrentUserInfo();
        this.memberSettingService.setPersonalWeeklyPlanTitle(organizationId, currentUser.getId(), title);
    }

    @Override
    public String getPersonalWeeklyPlanTitle(Long organizationId) {
        UserInfoDTO currentUser = getCurrentUserInfo();
        MemberSetting setting = this.memberSettingService.getPersonalWeeklyPlanTitle(organizationId, currentUser.getId());
        if (setting != null) {
            return setting.getValue();
        }
        //返回default的设置
        return WeeklyPlanServiceImpl.getDefaultPersonalWeeklyPlanTitle(currentUser.getName());
    }

    @Override
    public List<SimpleUserInfoDTO> listUsualWeeklyPlanReportReciver(Long organizationId) {
        UserInfoDTO currentUser = getCurrentUserInfo();
        return this.memberService.listUsualWeeklyPlanReportReciver(organizationId, currentUser.getId());
    }

    /*====================================用户站内消息操作========================================*/

    @Override
    public Integer countNotifycations() {
        UserInfoDTO currentUser = getCurrentUserInfo();
        return notifyManager.countSystemNotiry(currentUser.getId());
    }

    @Override
    public List<NotificationDTO> listNotifycations() {
        UserInfoDTO currentUser = getCurrentUserInfo();
        return notifyManager.listSystemNotify(currentUser.getId());
    }

    @Override
    public void markAllNotificationRead() {
        UserInfoDTO currentUser = getCurrentUserInfo();
        notifyManager.markAllSystemNotifyReadByUser(currentUser.getId());
    }


    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private OkrService okrService;
    @Autowired
    private WeeklyPlanService weeklyPlanService;

    @Autowired
    private WebSession webSession;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberSettingService memberSettingService;

    @Autowired
    private NotifyManager notifyManager;


    @Autowired
    private WxMpService wxMpService;


    @Value("${wechat.returnUrl}")
    private String returnUrl;


}
