package com.coreteam.okr.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: UserDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/15 16:18
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String UserName;
    private String email;
    private Boolean isActive;
    private String phoneNumber;
    private String lockoutEnd;
    private Integer accessFailCount;

}
