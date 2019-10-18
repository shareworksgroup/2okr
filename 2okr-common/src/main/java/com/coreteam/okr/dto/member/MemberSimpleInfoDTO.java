package com.coreteam.okr.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: MemberSimpleInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/14 16:44
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberSimpleInfoDTO {
    private Long userId;
    private String userName;
}
