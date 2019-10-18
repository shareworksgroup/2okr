package com.coreteam.okr.service;

import com.coreteam.okr.dto.objective.ObjectiveWeeklyPlanRegularEmailReportDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanCategoryDTO;
import com.coreteam.okr.entity.Invite;

/**
 * @ClassName: MSGService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/10 17:05
 * @Version 1.0.0
 */
public interface  MSGService {
    void sendRegisterInvitedMessge(Invite invite);

    void sendMemberJoinInvitedMessage(Invite invite, String toUser, String groupName);

    void sendWeeklyPlanReportRemind(WeeklyPlanCategoryDTO reporter, String title, String reciver, String email, Boolean attachment);

    void sendOrgRegularOkrAndWeeklyPlanReportEmail(ObjectiveWeeklyPlanRegularEmailReportDTO data, String email, String greeting);

}
