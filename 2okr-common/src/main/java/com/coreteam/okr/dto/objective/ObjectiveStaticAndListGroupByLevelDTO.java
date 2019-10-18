package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ObjectiveStaticAndListGroupByLevelDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/15 14:46
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveStaticAndListGroupByLevelDTO {
    private ObjectiveStatisticsDTO organizationStatistics;
    private ObjectiveStatisticsDTO teamStatistics;
    private ObjectiveStatisticsDTO memberStatistics;
    List<ObjectiveDTO> organizationObjectives;
    Map<String,List<ObjectiveDTO>> teamObjectives;
    List<ObjectiveDTO> memberObjectives;
}
