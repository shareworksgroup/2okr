package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: CloseObjectiveRequestDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/29 15:13
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloseObjectiveRequestDTO {
    private String comment;
    private Double confirmFinalResult;
}
