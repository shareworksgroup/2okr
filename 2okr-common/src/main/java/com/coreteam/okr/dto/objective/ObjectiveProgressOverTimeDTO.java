package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ObjectiveProgressOverTimeDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 14:54
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveProgressOverTimeDTO {
    private String date;
    private Integer onTrack;
    private Integer offTrack;
    private Integer atRisk;
    private Integer exceeded;
}
