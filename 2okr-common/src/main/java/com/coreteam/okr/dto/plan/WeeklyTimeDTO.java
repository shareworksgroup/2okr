package com.coreteam.okr.dto.plan;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName: WeeklyTimeDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/14 9:15
 * @Version 1.0.0
 */
@Data
public class WeeklyTimeDTO {

    private Integer week;
    private Date start;
    private Date end;
}
