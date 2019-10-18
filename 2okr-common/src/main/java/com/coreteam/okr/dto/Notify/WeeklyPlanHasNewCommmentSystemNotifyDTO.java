package com.coreteam.okr.dto.Notify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: WeeklyPlanHasNewCommmentSystemNotifyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/15 9:21
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanHasNewCommmentSystemNotifyDTO {
    private String title="Your Weekly Plan Has New Comments";
    private String message;
    private String userName;
    private Long userId;
    private String action;

    public WeeklyPlanHasNewCommmentSystemNotifyDTO( String commenter,String userName, Long userId,Long organizationId) {
        this.message = commenter+" give you Weekly Plan a new comment, You can view the detail";
        this.userName = userName;
        this.userId = userId;
        this.action = "/weekly/person/"+organizationId;
    }
}
