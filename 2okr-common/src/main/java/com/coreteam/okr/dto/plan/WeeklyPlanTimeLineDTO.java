package com.coreteam.okr.dto.plan;

import com.coreteam.okr.constant.BussinessLogOperateTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: WeeklyPlanTimeLineDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/14 11:01
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanTimeLineDTO {
    /**
     * the type of operate
     */
    private BussinessLogOperateTypeEnum operateType;

    /**
     * the operator user id
     */
    private Long operatorId;

    /**
     * description for operate
     */
    private String desc;

}
