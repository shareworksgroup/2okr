package com.coreteam.okr.dto.Notify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: KeyResultUpdateSystemNotifyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 13:42
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyResultUpdateSystemNotifyDTO {
    private String title="Your Key Result Update Now";
    private String message;
    private String userName;
    private Long userId;
    private String action;

    public KeyResultUpdateSystemNotifyDTO(String krName, String okrName,String userName, Long userId, Long okrId) {
        this.message = "The Key Result "+krName+" was updated, You can view the latest status";
        this.userName = userName;
        this.userId = userId;
        this.action = "/okrs/details/"+okrId;
    }
}
