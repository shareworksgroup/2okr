package com.coreteam.okr.dto.Authorization;

import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: ObjectiveResourcesDesc
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/30 15:45
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectiveResourcesDesc {
    private ObjectiveDTO objective;
    private List<MemberDTO> orgOwner;
    private List<MemberDTO> teamOwner;
}
