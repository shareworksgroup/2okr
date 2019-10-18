package com.coreteam.okr.dto.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPagedDemoListResponstDTO {
    private Integer id;

    private String name;
}
