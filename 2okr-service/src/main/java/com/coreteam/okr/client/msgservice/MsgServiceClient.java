package com.coreteam.okr.client.msgservice;

import com.coreteam.okr.dto.msg.SendEmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName: MsgServiceClient
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/29 10:48
 * @Version 1.0.0
 */
@FeignClient(value = "msg", url = "${msg-service.path}")
public interface MsgServiceClient {
    /**
     * 同步发送邮件
     * @param emailDTO
     */
    @RequestMapping(value = "/email}", method = RequestMethod.POST)
    void  sendEmail(@RequestBody SendEmailDTO emailDTO);

    /**
     * 异步发送邮件
     * @param emailDTO
     */
    @RequestMapping(value = "/email/asynchronous", method = RequestMethod.POST)
    void  sendEmailAsyn(@RequestBody SendEmailDTO emailDTO);
}
