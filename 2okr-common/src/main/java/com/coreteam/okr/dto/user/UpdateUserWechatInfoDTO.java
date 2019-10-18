package com.coreteam.okr.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: UpdateUserWechatInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/09/11 13:45
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserWechatInfoDTO {
    private Long userId;
    private String oauthId;
    private String accountName;
}
