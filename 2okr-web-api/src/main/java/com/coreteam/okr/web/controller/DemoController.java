package com.coreteam.okr.web.controller;

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
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Map;

/*@RestController
@RequestMapping("demo")
@AuditLogAnnotation(value = "demo接口")*/
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

    @GetMapping("/token-info")
    public Map<String, Object> getOAuth2Authentication(){
        Principal p = RequestUtils.getCurrentHttpServletRequest().getUserPrincipal();
        OAuth2AuthenticationDetails oauthDetails =  (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
        return details;
    }

    @GetMapping
    public PageInfo<GetPagedDemoListResponstDTO> getPagedDemoList(PageBaseDTO pageBaseDTO) {
        return demoService.getPagedDemoList(pageBaseDTO);
    }

    @GetMapping("/customer/error")
    public void customerError() throws CustomerException {
        throw new CustomerException("自定义逻辑异常");
    }

    @GetMapping("/system/error")
    public void systemError() throws Exception {
        throw new Exception("系统异常");
    }

    @GetMapping("/user/{id}")
    public UserDTO test(@PathVariable("id") @NotNull Long id){
        return userServiceClient.getUserInfoById(id);
    }

    @GetMapping("/msg/email")
    public void test2(){
    /*    GetOrgObjectiveRegularReportDTO query=new GetOrgObjectiveRegularReportDTO();
        query.setOrganizationId(121L);
        query.setUserId(94L);
        query.setUserName("sean");
        ObjectiveRegularEmailReportDTO data = okrService.prepareDataForOrgOKRRegularReport(query);
        msgService.sendOrgRegularOkrReportEmail(data,"sean.deng@shareworks.cn");*/

    }

    @GetMapping("/rabitmq/test")
    public void  test3(String content){
        String message="{\n" +
                "   \"type\":\"EMAIL\",\n" +
                "   \"data\":{\n" +
                "\t\t\t\"to\":\"XXXX@XXX.com\",\n" +
                "\t\t\t\"title\":\"会议通知\",\n" +
                "\t\t\t\"content\":\"今天下午2点半开会了  时间地点日下\"\t\n" +
                "\t\t  }\n" +
                "}";
         this.sender.send2MSG(message);
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
