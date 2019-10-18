package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: InsertObjectiveKeyResultCommentDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/09 14:55
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertObjectiveKeyResultCommentDTO {
    @NotNull
    private Long keyResultId;
    @NotBlank
    private String comment;

    private List<Long> mentions;
}
