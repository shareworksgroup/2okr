package com.coreteam.okr.dto.plantemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ThinkPad
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveWeeklyPlanTemplateItemDTO {
    private Long id;

    @NotBlank
    private String name;

    private Long templateId;

    @NotNull
    private Integer order;
    @NotBlank
    private String color;
    @NotNull
    private Boolean needCarry;

}
