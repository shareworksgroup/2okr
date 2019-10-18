package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: PlanCommentDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 15:30
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanCommentDTO {
    /**
     * id
     */
    private Long id;

    /**
     * comment
     */
    private String comment;

    /**
     * comment user
     */
    private Long commentUserId;

    private String commentUserName;
}
