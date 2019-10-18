package com.coreteam.okr.dto.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: UpdateTeamDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 10:37
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTeamDTO {

    private String name;

    private Long leaderId;

    private String leaderName;

/*    private Integer organizationId;*/

}
