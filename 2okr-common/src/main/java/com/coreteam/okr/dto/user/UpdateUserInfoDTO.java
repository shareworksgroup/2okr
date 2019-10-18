package com.coreteam.okr.dto.user;

import com.coreteam.okr.constant.UserLangTypeEnum;
import com.coreteam.okr.constant.UserThemeTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: UpdateUserInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/13 9:31
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserInfoDTO {
    private String name;
    private String email;
    private String phone;
    private UserThemeTypeEnum theme;
    private UserLangTypeEnum language;
}
