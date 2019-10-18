package com.coreteam.okr.dto.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDemoByIdResponseDTO {
    private Integer id;

    private String name;

    private Integer gmtCreateBy;

    private Date gmtCreate;

    private Integer gmtModifiedBy;

    private Date gmtModified;
}
