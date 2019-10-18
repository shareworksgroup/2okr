package com.coreteam.okr.service.impl;

import com.coreteam.okr.client.msgservice.MsgServiceClient;
import com.coreteam.okr.common.util.AESUtil;
import com.coreteam.okr.common.util.ExceptionUtil;
import com.coreteam.okr.common.util.PDFUtil;
import com.coreteam.okr.constant.EmailTemplate;
import com.coreteam.okr.constant.ObjectiveWeeklyPlanRegularReportEmailTemplate;
import com.coreteam.okr.constant.WeeklyPlanReportEmailTemplate;
import com.coreteam.okr.dto.msg.SendEmailDTO;
import com.coreteam.okr.dto.objective.ObjectiveWeeklyPlanRegularEmailReportDTO;
import com.coreteam.okr.dto.plan.WeeklyPlanCategoryDTO;
import com.coreteam.okr.entity.Invite;
import com.coreteam.okr.service.MSGService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MSGServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/10 18:54
 * @Version 1.0.0
 */
@Service
@Slf4j
public class MSGServiceImpl implements MSGService {

    @Value("${sso.regist-path}")
    private String registerUrl;

    @Value("${okr-service.invite-accept-path}")
    private String invitedAcceptUrl;

    @Value("${okr-service.login}")
    private String weeklyPlanReportView;

    @Value("${okr-service.weekly_report_path}")
    private String weeklyPlanReportPath;

    @Autowired
    private MsgServiceClient msgServiceClient;

    @Override
    public void sendRegisterInvitedMessge(Invite invite) {
        try {
            msgServiceClient.sendEmailAsyn(buildInviteRegisterEmail(invite));
        } catch (Exception e) {
            log.error("MSGServiceImpl send register email failer message is " + ExceptionUtil.stackTraceToString(e));
        }
    }

    @Override
    public void sendMemberJoinInvitedMessage(Invite invite, String toUser, String groupName) {
        try {
            msgServiceClient.sendEmailAsyn(buildInviteJoinEmail(invite, toUser, groupName));
        } catch (Exception e) {
            log.error("MSGServiceImpl send join email failer message is " + ExceptionUtil.stackTraceToString(e));
        }

    }

    @Override
    public void sendWeeklyPlanReportRemind(WeeklyPlanCategoryDTO weeklyPlanDTO, String title,String reciver, String email, Boolean attachment) {
        try {
            msgServiceClient.sendEmailAsyn(buildWeeklyPlanReportRemindEmail(weeklyPlanDTO,title, reciver, email, attachment));
        } catch (Exception e) {
            log.error("MSGServiceImpl send weekly plan report remind email failer message is " + ExceptionUtil.stackTraceToString(e));
        }

    }

    @Override
    public void sendOrgRegularOkrAndWeeklyPlanReportEmail(ObjectiveWeeklyPlanRegularEmailReportDTO data, String email,String greeting) {
        try {
            msgServiceClient.sendEmailAsyn(buildOrgRegularOkrAndWeeklyPlanReportEmail(data, email,greeting));
        } catch (Exception e) {
            log.error("MSGServiceImpl send Org Regular OKR Report Email failer message is " + ExceptionUtil.stackTraceToString(e));
        }
    }


    private SendEmailDTO buildInviteRegisterEmail(Invite invite) {
        SendEmailDTO dto = new SendEmailDTO();
        dto.setTo(invite.getEmail());
        dto.setTitle("2OKR Registration invitation");
        String greet = "Dear Sir or Madam,";
        String context = invite.getInviterName() + " invite you to join 2OKR, You can click the button or the link to complete you registration .";
        String action_url = registerUrl;
        String action = "Register";
        dto.setContent(EmailTemplate.template.replace("{greeting}", greet).replace("{context}", context).replace("{action_url}", action_url).replace("{action_url}", action_url).replace("{action_url}", action_url).replace("{action}", action));
        return dto;
    }

    private SendEmailDTO buildInviteJoinEmail(Invite invite, String toUser, String groupName) {
        SendEmailDTO dto = new SendEmailDTO();
        dto.setTo(invite.getEmail());
        dto.setTitle("2OKR Member invitation");
        String greet = "Dear " + toUser + ",";
        String context = invite.getInviterName() + " invite you to join the " + groupName + " in 2OKR,You can click the button or the link to join the team. ";
        String action_url = null;
        try {
            action_url = invitedAcceptUrl + "?info=" + URLEncoder.encode(AESUtil.encryptInvitedInfo(String.valueOf(invite.getId())), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String action = "Join The Team";
        dto.setContent(EmailTemplate.template.replace("{greeting}", greet).replace("{context}", context).replace("{action_url}", action_url).replace("{action_url}", action_url).replace("{action_url}", action_url).replace("{action}", action));
        return dto;

    }

    private SendEmailDTO buildWeeklyPlanReportRemindEmail(WeeklyPlanCategoryDTO weeklyPlanDTO, String title, String reciver, String reciverEmail, Boolean attachment) {
        SendEmailDTO dto = new SendEmailDTO();
        String content = WeeklyPlanReportEmailTemplate.transForm(weeklyPlanDTO,title);
        dto.setTo(reciverEmail);
        dto.setTitle("2OKR Personal WeeklyPlan Report");
        dto.setContent(content);

        if (attachment) {
            try {
                String fileName = "weekly_report_" + weeklyPlanDTO.getOwnerName() + "_" + weeklyPlanDTO.getWeek() + ".pdf";
                File file = PDFUtil.htmlToPDF(content, "weekly_report_" + weeklyPlanDTO.getOwnerName() + "_" + weeklyPlanDTO.getWeek() + ".pdf");
                InputStream inputStream = new FileInputStream(file);
                Map<String, byte[]> attachements = new HashMap<>();
                attachements.put(fileName, IOUtils.toByteArray(inputStream));
                dto.setAttachments(attachements);

            } catch (Exception e) {
                log.error(ExceptionUtil.stackTraceToString(e));
            }
        }

        return dto;
    }

    private SendEmailDTO buildOrgRegularOkrAndWeeklyPlanReportEmail(ObjectiveWeeklyPlanRegularEmailReportDTO data, String email,String greeting) {
        SendEmailDTO dto = new SendEmailDTO();
        String content = ObjectiveWeeklyPlanRegularReportEmailTemplate.transForm(data,greeting);
        dto.setTo(email);
        dto.setTitle("Weekly Plan Report from 2OKR");
        dto.setContent(content);
        /*if (true) {
            try {
                String fileName = "weekly_report_" + data.getUserName() + "_" + data.getReportTime()+System.currentTimeMillis() + ".pdf";
                File file = PDFUtil.htmlToPDF(content, fileName);
                InputStream inputStream = new FileInputStream(file);
                Map<String, byte[]> attachements = new HashMap<>();
                attachements.put(fileName, IOUtils.toByteArray(inputStream));
                dto.setAttachments(attachements);
            } catch (Exception e) {
                log.error(ExceptionUtil.stackTraceToString(e));
            }
        }*/
        return dto;
    }

}
