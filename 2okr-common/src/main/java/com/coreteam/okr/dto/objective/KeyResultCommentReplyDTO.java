package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: KeyResultCommentReplyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/23 10:26
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyResultCommentReplyDTO {

    private Long id;

    private Long commentId;

    private Long replayUserId;

    private String replayUserName;

    private String context;

    private Date createdAt;

}
