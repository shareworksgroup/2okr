package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ListUserObjectiveAlignmentDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/31 8:30
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListUserObjectiveAlignmentDTO {
    private ObjectiveLevelEnum level;
}
