package com.coreteam.okr.dto.objective;

import com.coreteam.okr.constant.BussinessLogOperateTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: ObjectiveTimelineDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/29 15:15
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveTimelineDTO {
    private BussinessLogOperateTypeEnum operateType;

    /**
     * the operator user id
     */
    private Long operatorId;

    /**
     * description for operate
     */
    private String desc;

    private Date createdAt;

}
