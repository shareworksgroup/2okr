package com.coreteam.okr.dto.Notify;

import com.coreteam.okr.dto.objective.ObjectiveWeeklyPlanRegularEmailReportDTO;
import lombok.Data;

@Data
public class MemberWeeklyPlanRegularReportEmailNotifyDTO {
    private String email;
    private ObjectiveWeeklyPlanRegularEmailReportDTO data;
    private String gretting;

    public MemberWeeklyPlanRegularReportEmailNotifyDTO(String email, ObjectiveWeeklyPlanRegularEmailReportDTO data,String gretting) {
        this.email = email;
        this.data = data;
        this.gretting=gretting;
    }

    public MemberWeeklyPlanRegularReportEmailNotifyDTO() {
    }
}
