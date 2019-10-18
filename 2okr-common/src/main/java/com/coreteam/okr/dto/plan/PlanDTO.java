package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: PlanDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 10:38
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanDTO {

    private Long id;

    /**
     * 描述
     */
    private String desc;

    /**
     * owner id
     */
    private Long ownerId;

    /**
     * owner name
     */
    private String ownerName;

    /**
     * 是否私有
     */
    private Byte isPrivated;

    /**
     * 截止时间
     */
    private Date dueDate;


    private List<ObjectiveDTO> linkedObjectives;


}
