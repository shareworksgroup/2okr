package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: UpdateObjectiveKeyResulStatusDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 18:21
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateObjectiveKeyResulStatusDTO {
    @NotNull
    private Long id;

    @NotNull
    private Long objectiveId;
    /**
     * 初始值
     */
    @NotNull
    private Double metricStartValue;

    /**
     * 结束值
     */
    @NotNull
    private Double metricEndValue;


    /**
     * 当前值
     */
    @NotNull
    private Double value;


    /**
     * 当前keyresult的权重
     */
    @NotNull
    private Double weight;

}
