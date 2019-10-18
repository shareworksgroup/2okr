package com.coreteam.okr.dto.objective;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName: UpdateObjectiveStatusDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 13:23
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateObjectiveStatusDTO {
    @NotNull
    private Long id;
    @NotNull
    private Date periodStartTime;
    @NotNull
    private Date periodEndTime;
}
