package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: KeyResultController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Data
public class PlanObjectiveLink extends BaseEntity implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * plan id
     */
    private Long planId;

    /**
     * objective id
     */
    private Long objectiveId;

    private static final long serialVersionUID = 1L;


}