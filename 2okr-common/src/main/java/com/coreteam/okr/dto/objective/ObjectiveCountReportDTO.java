package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: ObjectiveCountReportDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 18:33
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveCountReportDTO {
    private Integer onTrack;
    private Integer offTrack;
    private Integer atRisk;
    private Integer exceeded;
    private Integer organizationNum;
    private Integer teamNum;
    private Integer memberNum;

}
