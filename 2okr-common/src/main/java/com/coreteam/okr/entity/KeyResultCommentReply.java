package com.coreteam.okr.entity;

import lombok.Data;


/**
 * key result comment reply
 */

@Data
public class KeyResultCommentReply extends BaseEntity {

    private Long id;

    private Long commentId;

    private Long replayUserId;

    private String replayUserName;

    private String context;


    private static final long serialVersionUID = 1L;

}