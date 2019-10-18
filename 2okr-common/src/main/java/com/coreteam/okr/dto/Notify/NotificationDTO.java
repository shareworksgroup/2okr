package com.coreteam.okr.dto.Notify;

import com.coreteam.okr.constant.NotificationStateEnum;
import com.coreteam.okr.constant.SystemNotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: NotificationDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/11 16:11
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO {
    private Long id;

    private Long userId;

    private NotificationStateEnum state;

    private SystemNotificationType type;

    private Date createdAt;

    private MessageDTO message;
}
