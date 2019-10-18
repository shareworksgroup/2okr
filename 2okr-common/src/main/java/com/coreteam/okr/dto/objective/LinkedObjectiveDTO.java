package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: LinkedObjectiveDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/29 10:55
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedObjectiveDTO {
    @NotNull
    private Long linkedObjectiveId;
}
