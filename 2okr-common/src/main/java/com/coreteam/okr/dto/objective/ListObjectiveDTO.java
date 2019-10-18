package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: ListObjectiveDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/12 14:27
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListObjectiveDTO {
    private Long organizationId;
    private Long businessId;
    private Integer pageNumber;
    private Integer pageSize;
    private Date start;
    private Date end;
    private String level;
}
