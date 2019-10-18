package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ObjectiveChildrenDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/22 14:02
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveChildrenDTO {
    private List<ObjectiveDTO> orgOkrs;
    private List<ObjectiveDTO> memberOkrs;
    private Map<String,List<ObjectiveDTO>> teamOkrs;
}
