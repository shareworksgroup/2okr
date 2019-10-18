package com.coreteam.okr.dto.Authorization;

import com.coreteam.okr.dto.member.MemberDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: TeamResourcesDescDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/29 11:23
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamResourcesDescDTO {
   private List<MemberDTO> orgOwner;
   private List<MemberDTO> teamOwner;
}
