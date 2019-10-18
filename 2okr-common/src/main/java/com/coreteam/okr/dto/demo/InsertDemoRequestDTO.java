package com.coreteam.okr.dto.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertDemoRequestDTO {

    @NotBlank
    private String name;
}
