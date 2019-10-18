package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: EditPlanCommentDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 11:46
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditPlanCommentDTO {
    @NotNull
    private String comments;
}
