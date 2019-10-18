package com.coreteam.okr.manager.impl;

import com.alibaba.fastjson.JSON;
import com.coreteam.okr.constant.NotificationStateEnum;
import com.coreteam.okr.constant.SystemNotificationType;
import com.coreteam.okr.dto.Notify.*;
import com.coreteam.okr.manager.Notify;
import com.coreteam.okr.manager.NotifyManager;
import com.coreteam.okr.service.MSGService;
import com.coreteam.okr.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: NotifyManagerImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 10:12
 * @Version 1.0.0
 */
@Service
public class NotifyManagerImpl implements NotifyManager {

    @Autowired
    private NotifyService systemNotifyService;

    @Autowired
    private MSGService msgService;


    @Override
    public void sendNotify(Notify noify) {
        switch (noify.type()) {
            case EMAIL:
                sendEmailNotify(noify);
                break;
            case SMS:
                break;
            case SYSTEM:
                sendSystemNotify(noify);
                break;
            case WEIXIN:
                break;
        }
    }
    private void sendSystemNotify(Notify noify) {
        NotificationDTO dto=buildNotificationDTO(noify);
        if(dto!=null){
            saveSystemNotification(dto);
        }
    }

    private void sendEmailNotify(Notify noify) {
        switch (noify.businessType()) {
            case EMAIL_JOIN:
                MemberInvitedEmailNotifyDTO emailJoin= (MemberInvitedEmailNotifyDTO) noify.message();
                msgService.sendMemberJoinInvitedMessage(emailJoin.getInvite(),emailJoin.getToUser(),emailJoin.getGroupName());
                break;
            case EMAIL_REGISTER:
                RegisterInvitedEmailNotifyDTO emailRegister = (RegisterInvitedEmailNotifyDTO) noify.message();
                msgService.sendRegisterInvitedMessge(emailRegister.getInvite());
                break;
            case EMAIL_PERSONAL_WEEKLY_PLAN_REPORT:
                PersonalWeeklyPlanReportEmailNotifyDTO emailPersonalWeeklyPlanReport = (PersonalWeeklyPlanReportEmailNotifyDTO) noify.message();
                msgService.sendWeeklyPlanReportRemind(emailPersonalWeeklyPlanReport.getReporter(),emailPersonalWeeklyPlanReport.getTitle(),emailPersonalWeeklyPlanReport.getReciver(),emailPersonalWeeklyPlanReport.getEmail(),emailPersonalWeeklyPlanReport.getAttachment());
                break;
            case EMAIL_ORG_REGULAR_REPORT:
                MemberWeeklyPlanRegularReportEmailNotifyDTO emailOrgRegularReport = (MemberWeeklyPlanRegularReportEmailNotifyDTO)noify.message();
                msgService.sendOrgRegularOkrAndWeeklyPlanReportEmail(emailOrgRegularReport.getData(),emailOrgRegularReport.getEmail(),emailOrgRegularReport.getGretting());
                break;
            default:
                break;
        }
    }

    private NotificationDTO buildNotificationDTO(Notify noify){
        NotificationDTO notificationDTO=new NotificationDTO();
        switch (noify.businessType()){
            case SYSTEM_OBJECTIVE_UPDATE:
                ObjectiveUpdateSystemNotifyDTO objectiveUpdateSystemNotifyDTO= (ObjectiveUpdateSystemNotifyDTO) noify.message();
                notificationDTO.setUserId(objectiveUpdateSystemNotifyDTO.getUserId());
                notificationDTO.setState(NotificationStateEnum.UNREAD);
                notificationDTO.setType(SystemNotificationType.OBJECTIVE_UPDATE);
                MessageDTO systemObjectiveUpdate=new MessageDTO();
                systemObjectiveUpdate.setTitle(objectiveUpdateSystemNotifyDTO.getTitle());
                systemObjectiveUpdate.setMessage(JSON.parseObject(JSON.toJSONString(objectiveUpdateSystemNotifyDTO)));
                notificationDTO.setMessage(systemObjectiveUpdate);
                break;
            case SYSTEM_OBJECTIVE_OFF_TRACE:
                ObjectiveOffTraceSystemNotifyDTO objectiveOffTraceSystemNotifyDTO= (ObjectiveOffTraceSystemNotifyDTO) noify.message();
                notificationDTO.setUserId(objectiveOffTraceSystemNotifyDTO.getUserId());
                notificationDTO.setState(NotificationStateEnum.UNREAD);
                notificationDTO.setType(SystemNotificationType.OBJECTIVE_UPDATE);
                MessageDTO systemObjectiveOffTrace=new MessageDTO();
                systemObjectiveOffTrace.setTitle(objectiveOffTraceSystemNotifyDTO.getTitle());
                systemObjectiveOffTrace.setMessage(JSON.parseObject(JSON.toJSONString(objectiveOffTraceSystemNotifyDTO)));
                notificationDTO.setMessage(systemObjectiveOffTrace);
                break;
            case SYSTEM_KEY_RESULT_UPDATE:
                KeyResultUpdateSystemNotifyDTO keyResultUpdateSystemNotifyDTO= (KeyResultUpdateSystemNotifyDTO) noify.message();
                notificationDTO.setUserId(keyResultUpdateSystemNotifyDTO.getUserId());
                notificationDTO.setState(NotificationStateEnum.UNREAD);
                notificationDTO.setType(SystemNotificationType.KEY_RESULT_UPDATE);
                MessageDTO systemKeyResultUpdate=new MessageDTO();
                systemKeyResultUpdate.setTitle(keyResultUpdateSystemNotifyDTO.getTitle());
                systemKeyResultUpdate.setMessage(JSON.parseObject(JSON.toJSONString(keyResultUpdateSystemNotifyDTO)));
                notificationDTO.setMessage(systemKeyResultUpdate);
                break;

            case SYSTEM_KEY_RESULT_HAS_NEW_COMMENT:
                KeyResultHasNewCommentSystemNotifyDTO keyResultHasNewCommentSystemNotifyDTO= (KeyResultHasNewCommentSystemNotifyDTO) noify.message();
                notificationDTO.setUserId(keyResultHasNewCommentSystemNotifyDTO.getUserId());
                notificationDTO.setState(NotificationStateEnum.UNREAD);
                notificationDTO.setType(SystemNotificationType.KEY_RESULT_HAS_NEW_COMMENT);
                MessageDTO systemKeyResultNewComemnt=new MessageDTO();
                systemKeyResultNewComemnt.setTitle(keyResultHasNewCommentSystemNotifyDTO.getTitle());
                systemKeyResultNewComemnt.setMessage(JSON.parseObject(JSON.toJSONString(keyResultHasNewCommentSystemNotifyDTO)));
                notificationDTO.setMessage(systemKeyResultNewComemnt);
                break;
            case SYSTEM_WEEKLY_PLAN_HAS_NEW_COMMENT:
                WeeklyPlanHasNewCommmentSystemNotifyDTO weeklyPlanHasNewCommmentSystemNotifyDTO= (WeeklyPlanHasNewCommmentSystemNotifyDTO) noify.message();
                notificationDTO.setUserId(weeklyPlanHasNewCommmentSystemNotifyDTO.getUserId());
                notificationDTO.setState(NotificationStateEnum.UNREAD);
                notificationDTO.setType(SystemNotificationType.WEEKLYPLAN_HAS_NEW_COMMENT);
                MessageDTO systemWeeklyPlanNewComemnt=new MessageDTO();
                systemWeeklyPlanNewComemnt.setTitle(weeklyPlanHasNewCommmentSystemNotifyDTO.getTitle());
                systemWeeklyPlanNewComemnt.setMessage(JSON.parseObject(JSON.toJSONString(weeklyPlanHasNewCommmentSystemNotifyDTO)));
                notificationDTO.setMessage(systemWeeklyPlanNewComemnt);
                break;
                default:
                    return null;
        }
        return notificationDTO;
    }

    private void saveSystemNotification(NotificationDTO dto) {
        systemNotifyService.saveNotification(dto);
    }

    @Override
    public List<NotificationDTO> listSystemNotify(Long userId) {
        return systemNotifyService.listNotifyicationByUser(userId);
    }

    @Override
    public void markSystemNotifyRead(Long id) {
        systemNotifyService.markRead(id);
    }

    @Override
    public void markAllSystemNotifyReadByUser(Long id) {
        systemNotifyService.markAllNotificationReadByUser(id);
    }

    @Override
    public Integer countSystemNotiry(Long userId) {
        return systemNotifyService.countNotifycationByUser(userId);
    }


}
