package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * key_result_comment
 *
 * @author
 */
@Data
public class KeyResultComment extends BaseEntity implements Serializable {
    private Long id;

    private Long keyResultId;

    private Long commentUserId;

    private String commentUserName;

    private String comment;


    private static final long serialVersionUID = 1L;
}