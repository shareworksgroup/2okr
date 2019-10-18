package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: AlignmentObjectiveDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/31 8:27
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlignmentObjectiveDTO {
    private List<ObjectiveDTO> organizationObjectives;
    private List<ObjectiveDTO> teamObjectives;
    private List<ObjectiveDTO> memberObjectives;
}
