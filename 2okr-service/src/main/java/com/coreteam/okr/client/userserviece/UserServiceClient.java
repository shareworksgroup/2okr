package com.coreteam.okr.client.userserviece;

import com.coreteam.okr.dto.user.UpdateRemoteUserInfoDTO;
import com.coreteam.okr.dto.user.UpdateUserWechatInfoDTO;
import com.coreteam.okr.dto.user.UserDTO;
import com.coreteam.okr.dto.user.UserWechatInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: UserServiceClient
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/15 16:12
 * @Version 1.0.0
 */
@FeignClient(value = "user", url = "${user-service.path}")
public interface UserServiceClient {
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    UserDTO getUserInfoById(@PathVariable("id") Long id);

    @RequestMapping(value = "/users/filter", method = RequestMethod.GET)
    UserDTO getUserByEmail(@RequestParam("email") String email);

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    void  updateUserInfo(@PathVariable("id") Long id, @RequestBody UpdateRemoteUserInfoDTO userInfoDTO);

    @RequestMapping(value = "/users/{id}/social/wechat", method = RequestMethod.GET)
    UserWechatInfoDTO getUserWechatInfoById(@PathVariable("id") Long id);

    @RequestMapping(value = "/users/{id}/social/wechat", method = RequestMethod.PUT)
    void updateUserWechatInfo(@PathVariable("id") Long id, @RequestBody UpdateUserWechatInfoDTO updateUserWechatInfoDTO);

    @RequestMapping(value = "/users/{id}/social/wechat", method = RequestMethod.DELETE)
    void deleteUserWechatInfo(@PathVariable("id") Long id);

}
