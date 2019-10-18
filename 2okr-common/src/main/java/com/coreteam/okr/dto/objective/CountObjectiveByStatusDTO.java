package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: CountObjectiveByStatusDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/12 15:09
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountObjectiveByStatusDTO {
    private Long businessId;
    private Date start;
    private Date end;
    private String level;
    private Long organizationId;
}
