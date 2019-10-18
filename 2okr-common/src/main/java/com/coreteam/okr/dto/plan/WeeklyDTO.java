package com.coreteam.okr.dto.plan;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class WeeklyDTO {
    @NotNull
    private Integer week;

    private List<String> externalEmails;
}
