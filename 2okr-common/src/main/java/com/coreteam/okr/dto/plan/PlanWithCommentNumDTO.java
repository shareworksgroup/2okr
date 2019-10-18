package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: PlanWithCommentNumDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/22 10:29
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanWithCommentNumDTO {
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

    private Integer commentNums;
}
