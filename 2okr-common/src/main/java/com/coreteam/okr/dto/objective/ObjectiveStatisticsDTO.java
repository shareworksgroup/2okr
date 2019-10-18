package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: ObjectiveStatisticsDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 8:54
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveStatisticsDTO {
    private Integer onTrack;
    private Integer offTrack;
    private Integer atRisk;
    private Integer exceeded;
    private Double avgProgress;
    private List<ObjectiveProgressOverTimeDTO> progressOverTime;


    public ObjectiveStatisticsDTO(Integer onTrack, Integer offTrack, Integer atRisk, Integer exceeded,Double avgProgress) {
        this.onTrack = onTrack;
        this.offTrack = offTrack;
        this.atRisk = atRisk;
        this.exceeded = exceeded;
        this.avgProgress=avgProgress;
    }

    public ObjectiveStatisticsDTO() {
    }
}
