package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: UpdateObjectiveKeyResulReviewDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/19 13:58
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateObjectiveKeyResulReviewDTO {
    @NotNull
    private Long id;
    @NotNull
    private Long objectiveId;

    @Min(value = 0)
    @Max(value = 100)
    private Integer confidenceLevel;

    @Min(value = 0)
    @Max(value = 1)
    private Double midTermRating;

    @Min(value = 0)
    @Max(value = 1)
    private Double finalRating;

    private String summary;

}
