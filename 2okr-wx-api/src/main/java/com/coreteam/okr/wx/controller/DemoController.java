package com.coreteam.okr.wx.controller;

import com.coreteam.core.dto.PageBaseDTO;
import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.RequestUtils;
import com.coreteam.okr.client.msgservice.MsgServiceClient;
import com.coreteam.okr.client.userserviece.UserServiceClient;
import com.coreteam.okr.dto.demo.GetDemoByIdResponseDTO;
import com.coreteam.okr.dto.demo.GetPagedDemoListResponstDTO;
import com.coreteam.okr.dto.demo.InsertDemoRequestDTO;
import com.coreteam.okr.dto.user.UserDTO;
import com.coreteam.okr.sender.RabbitMqSender;
import com.coreteam.okr.service.DemoService;
import com.coreteam.okr.service.MSGService;
import com.coreteam.okr.service.OkrService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("demo")
@AuditLogAnnotation(value = "demo接口")
@Slf4j
public class DemoController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private RabbitMqSender sender;

    @Value("${msg-service.path}")
    private String url;

    @GetMapping("/{id}")
    public GetDemoByIdResponseDTO getDemoById(@PathVariable("id") int id) {
        log.info("记录日志,请求测试");
        return demoService.getDemoById(id);
    }

    @PostMapping
    public void insertDemo(@RequestBody @Valid InsertDemoRequestDTO requestDTO){
        demoService.insertDemo(requestDTO);
    }

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private MsgServiceClient msgServiceClient;

    @Autowired
    private MSGService msgService;

    @Autowired
    private OkrService okrService;

}
