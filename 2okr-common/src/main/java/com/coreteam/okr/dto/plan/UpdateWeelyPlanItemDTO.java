package com.coreteam.okr.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: UpdateWeelyPlanItemDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 10:53
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateWeelyPlanItemDTO {
    private Long categorieId;
    private String color;
    private String desc;
    private Date dueDate;
}
