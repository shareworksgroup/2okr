package com.coreteam.okr.dto.plantemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: WeeklyPlanTemplateDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/17 13:40
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyPlanTemplateDTO {
    /**
     * primary key
     */
    private Long id;

    private String templateName;

    /**
     * SYSTEM,ORGANIZATION,TEAM,MEMBER
     */
    private String type;

    private Long entityId;

    /**
     * template is used
     */
    private Byte enable;

    private List<WeeklyPlanTemplateItemDTO> items;

}
