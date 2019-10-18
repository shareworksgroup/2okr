package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: InsertObjectiveKeyResultCommentReplyDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/23 10:43
 * @Version 1.0.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertObjectiveKeyResultCommentReplyDTO {
    @NotNull
    private Long commentId;
    @NotBlank
    private String replay;

    private List<Long> mentions;
}
