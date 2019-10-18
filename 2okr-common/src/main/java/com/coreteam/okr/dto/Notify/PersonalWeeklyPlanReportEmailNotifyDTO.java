package com.coreteam.okr.dto.Notify;

import com.coreteam.okr.dto.plan.WeeklyPlanCategoryDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: PersonalWeeklyPlanReportEmailNotifyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 11:32
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalWeeklyPlanReportEmailNotifyDTO {
    private WeeklyPlanCategoryDTO reporter;
    private String reciver;
    private String email;
    private Boolean attachment;
    private String title;

    public PersonalWeeklyPlanReportEmailNotifyDTO(WeeklyPlanCategoryDTO reporter, String reciver, String email, Boolean attachment,String title) {
        this.reporter = reporter;
        this.reciver = reciver;
        this.email = email;
        this.attachment = attachment;
        this.title=title;
    }

    public PersonalWeeklyPlanReportEmailNotifyDTO() {
    }
}

