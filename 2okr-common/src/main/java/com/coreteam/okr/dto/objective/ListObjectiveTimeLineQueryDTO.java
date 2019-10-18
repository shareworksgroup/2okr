package com.coreteam.okr.dto.objective;

import com.coreteam.core.dto.PageBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: ListObjectiveTimeLineQueryDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/29 15:16
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListObjectiveTimeLineQueryDTO extends PageBaseDTO {
    @NotNull
    private Long objectiveId;
}
