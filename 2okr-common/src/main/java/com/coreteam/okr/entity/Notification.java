package com.coreteam.okr.entity;

import com.coreteam.okr.constant.NotificationStateEnum;
import com.coreteam.okr.constant.SystemNotificationType;
import lombok.Data;

import java.io.Serializable;

/**
 * notification
 *
 * @author
 */
@Data
public class Notification extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    private Long messageId;

    private Long userId;

    private NotificationStateEnum state;

    private SystemNotificationType type;

    private static final long serialVersionUID = 1L;

    public Notification(Long messageId, Long userId, NotificationStateEnum state, SystemNotificationType type) {
        this.messageId = messageId;
        this.userId = userId;
        this.state = state;
        this.type = type;
    }

    public Notification() {

    }

}