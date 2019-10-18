package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName: ObjectiveStaticAndListQueryDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/15 14:24
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveStaticAndListQueryDTO {

    @NotNull
    private Date start;
    @NotNull
    private Date end;
    @NotNull
    private Long organizationId;

    private String search;

    private Long teamId;

    private Long userId;
}
