package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: ObjectiveKeyResultCommentDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/4/2
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveKeyResultCommentDTO {
    private Long id;

    private Long keyResultId;

    private Long commentUserId;

    private String commentUserName;

    private String comment;

    private Date createdAt;

    private List<KeyResultCommentReplyDTO> replyList = new ArrayList<>();
}
