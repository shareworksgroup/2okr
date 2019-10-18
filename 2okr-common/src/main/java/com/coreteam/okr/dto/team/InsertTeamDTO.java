package com.coreteam.okr.dto.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: InsertTeamDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 10:37
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertTeamDTO {

    @NotNull
    private String name;

    @NotNull
    private Long leaderId;

    @NotNull
    private String leaderName;

    @NotNull
    private Long organizationId;

    private List<Long> members;

}
