package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: GetUserObjectiveReportDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/12 15:51
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserObjectiveReportDTO {
    private Long organizationId;
    private Long userId;
    private Date start;
    private Date end;
}
