package com.coreteam.okr.dto.Notify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ObjectiveUpdateSystemNotifyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 13:04
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveUpdateSystemNotifyDTO {
    private String title="Your OKR Update Now";
    private String message;
    private String userName;
    private Long userId;
    private String action;

    public ObjectiveUpdateSystemNotifyDTO(String okrName, String userName, Long userId, Long okrId) {
        this.message = "Your OKR "+ okrName+" was updated, You can view the latest status";
        this.userName = userName;
        this.userId = userId;
        this.action = "/okrs/details/"+okrId;
    }

}
