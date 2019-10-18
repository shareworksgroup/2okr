package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: GetOrgObjectiveRegularReportDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/13 14:57
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetOrgObjectiveRegularReportDTO {
    private Long organizationId;
    private Long userId;
    private String userName;
}
