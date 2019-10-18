package com.coreteam.okr.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: UserInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/11 8:21
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String jobPosition;
    private String role;
    private String theme;
    private String language;


}
