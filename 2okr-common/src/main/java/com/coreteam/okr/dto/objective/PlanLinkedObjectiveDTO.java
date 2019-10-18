package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: PlanLinkedObjectiveDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/22 17:10
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanLinkedObjectiveDTO {
    @NotNull
    private List<Long> objectives;
}
