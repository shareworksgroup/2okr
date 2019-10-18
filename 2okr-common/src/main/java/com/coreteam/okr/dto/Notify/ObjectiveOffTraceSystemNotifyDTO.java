package com.coreteam.okr.dto.Notify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ObjectiveOffTraceSystemNotifyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 13:47
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveOffTraceSystemNotifyDTO {
    private String title="Reminder to update you OKRs";
    private String message;
    private String userName;
    private Long userId;
    private String action;

    public ObjectiveOffTraceSystemNotifyDTO(String okrName, String userName, Long userId, Long okrId) {
        this.message = "Your OKR "+ okrName+" was off track, Update Now";
        this.userName = userName;
        this.userId = userId;
        this.action = "/okrs/details/"+okrId;
    }
}
