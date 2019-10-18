package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.ObjectiveNotConbineKRDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: WeeklyPlanItemViewDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/24 13:40
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanItemViewDTO {
    private Long itemId;

    private String desc;

    private Date dueDate;

    private Long categorieId;

    private String color;

    private Integer carriedOverAge;

    private Long ownerId;

    private String ownerName;

    private Integer commentNum;

    private List<ObjectiveNotConbineKRDTO> linkedObjectives;


}
