package com.coreteam.okr.web.controller;

import com.coreteam.okr.manager.NotifyManager;
import com.coreteam.okr.service.UserService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: SystemNotifyController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/11 16:08
 * @Version 1.0.0
 */
@RestController
@RequestMapping("notifycation")
@AuditLogAnnotation(value = "notifycation接口")
@Slf4j
public class SystemNotifyController {

    @Autowired
    private NotifyManager notifyManager;

    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    @ApiOperation("将站内消息标记为已读")
    public void markNotificationRead(@PathVariable("id") @NotNull Long id) {
        notifyManager.markSystemNotifyRead(id);
    }

    @PutMapping("/all/mark/read")
    @ApiOperation("将站内消息标记为已读")
    public void markAllNotificationRead(){
        userService.markAllNotificationRead();
    }


}
