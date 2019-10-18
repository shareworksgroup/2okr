package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: UpdateObjectiveKeyResulProgressDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/30 12:35
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateObjectiveKeyResulProgressDTO {
    @NotNull
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Double progress;
}
