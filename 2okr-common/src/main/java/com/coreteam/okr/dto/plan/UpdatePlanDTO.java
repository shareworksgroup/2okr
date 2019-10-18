package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: UpdatePlanDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 14:57
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePlanDTO {
    private String desc;
    private Date dueDate;
}
