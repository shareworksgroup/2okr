package com.coreteam.okr.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: UpdateRemoteUserInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/30 10:18
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRemoteUserInfoDTO {
    @NotNull
    private String userName;
    private String phoneNumber;

}
