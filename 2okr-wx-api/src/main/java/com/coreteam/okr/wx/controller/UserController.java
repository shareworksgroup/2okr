package com.coreteam.okr.wx.controller;

import com.coreteam.okr.dto.user.UpdateUserInfoDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.service.UserService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName: UserController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/06 11:11
 * @Version 1.0.0
 */

@RestController
@RequestMapping("users")
@AuditLogAnnotation(value = "users接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation("获取当前的用户信息详情")
    public UserInfoDTO getUserInfo() {
        UserInfoDTO dto = userService.getUserInfoById(userService.getCurrentUserInfo().getId());
        return dto == null ? new UserInfoDTO() : dto;
    }


    @PutMapping()
    @ApiOperation("更新当前用户基本信息")
    public void updateUserInfo(@RequestBody @Valid UpdateUserInfoDTO userInfoDTO) {
        userService.updateUserInfo(userInfoDTO);
    }

}
