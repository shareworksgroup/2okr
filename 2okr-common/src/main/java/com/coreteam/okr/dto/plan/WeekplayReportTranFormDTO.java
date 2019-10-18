package com.coreteam.okr.dto.plan;

import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: WeekplayReportTranFormDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/17 20:03
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekplayReportTranFormDTO {

    private List<WeeklyPlanDTO> list;

    private WeeklyPlanTemplateDTO template;

}
