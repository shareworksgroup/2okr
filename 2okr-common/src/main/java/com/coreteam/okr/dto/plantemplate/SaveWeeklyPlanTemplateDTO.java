package com.coreteam.okr.dto.plantemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ThinkPad
 * 插入模板
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveWeeklyPlanTemplateDTO {
    /**
     * primary key
     */
    private Long id;

    @NotBlank
    private String templateName;

    /**
     * SYSTEM,ORGANIZATION,TEAM,MEMBER
     */
    @NotBlank
    private String type;

    @NotNull
    private Long entityId;

    @NotEmpty
    private List<SaveWeeklyPlanTemplateItemDTO> items;
}
