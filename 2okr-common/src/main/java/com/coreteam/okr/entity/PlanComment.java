package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: KeyResultController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Data
public class PlanComment extends BaseEntity implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * plan_item_id
     */
    private Long planId;

    /**
     * comment
     */
    private String comment;

    /**
     * comment user
     */
    private Long commentUserId;

    private String commentUserName;

    public PlanComment() {
    }

    public PlanComment(Long planId, String comment, Long commentUserId, String commentUserName) {
        this.planId = planId;
        this.comment = comment;
        this.commentUserId = commentUserId;
        this.commentUserName = commentUserName;
    }
}