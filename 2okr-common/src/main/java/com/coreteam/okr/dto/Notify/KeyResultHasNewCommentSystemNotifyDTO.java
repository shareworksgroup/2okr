package com.coreteam.okr.dto.Notify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: KeyResultHasNewCommentSystemNotifyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/14 14:18
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyResultHasNewCommentSystemNotifyDTO {
    private String title="Your Key Result Has New Comments";
    private String message;
    private String userName;
    private Long userId;
    private String action;

    public KeyResultHasNewCommentSystemNotifyDTO(String krName, String commenter,String userName, Long userId, Long okrId) {
        this.message = commenter+" give you Key Result "+krName+" a new comment, You can view the detail";
        this.userName = userName;
        this.userId = userId;
        this.action = "/okrs/details/"+okrId;
    }
}
