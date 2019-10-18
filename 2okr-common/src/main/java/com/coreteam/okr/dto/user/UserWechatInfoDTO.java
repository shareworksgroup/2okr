package com.coreteam.okr.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: UserWechatInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/11 13:43
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserWechatInfoDTO {
    private Long id;
    private Long userId;
    private String oauthId;
    private String oauthType;
    private String accountName;
}
