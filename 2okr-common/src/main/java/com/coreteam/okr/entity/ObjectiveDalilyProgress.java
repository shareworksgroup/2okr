package com.coreteam.okr.entity;

import com.coreteam.okr.constant.ObjectiveBusinessStatusEnum;
import lombok.Data;

import java.io.Serializable;


/**
 * @ClassName: KeyResultController
 * @Description objective 每天的progress记录表
 * @Author sean.deng
 * @Date 2019/04/11 14:46
 * @Version 1.0.0
 */
@Data
public class ObjectiveDalilyProgress extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    /**
     * objective id 
     */
    private Long objectiveId;

    /**
     * record date yyyy-mm-dd
     */
    private String date;

    /**
     * objective progress
     */
    private Double progress;

    /**
     * objective expect_progress
     */
    private Double expectProgress;


    private ObjectiveBusinessStatusEnum status;

}