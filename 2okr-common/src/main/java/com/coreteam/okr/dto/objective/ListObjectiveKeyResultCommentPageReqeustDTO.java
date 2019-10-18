package com.coreteam.okr.dto.objective;

import com.coreteam.core.dto.PageBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: ListObjectiveKeyResultCommentPageReqeustDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/4/2
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListObjectiveKeyResultCommentPageReqeustDTO extends PageBaseDTO {
    @NotNull
    private Long keyResultId;
}
