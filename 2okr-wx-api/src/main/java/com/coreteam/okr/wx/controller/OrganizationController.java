package com.coreteam.okr.wx.controller;

import com.coreteam.okr.common.util.DateUtil;
import com.coreteam.okr.constant.ObjectiveLevelEnum;
import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.objective.InsertObjectiveDTO;
import com.coreteam.okr.dto.objective.InsertObjectiveKeyResultDTO;
import com.coreteam.okr.dto.objective.ObjectiveKeyResultDTO;
import com.coreteam.okr.dto.objective.UpdateObjectiveKeyResulProgressDTO;
import com.coreteam.okr.dto.organization.DefaultOrgnazationSettingDTO;
import com.coreteam.okr.dto.organization.InsertOrganizationDTO;
import com.coreteam.okr.dto.organization.OrganizationDTO;
import com.coreteam.okr.dto.organization.UpdateOrganizationDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Objective;
import com.coreteam.okr.service.*;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: OrganizationController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/06 10:17
 * @Version 1.0.0
 */

@RestController
@RequestMapping("organizations")
@AuditLogAnnotation(value = "organization weixin api 接口")
@Slf4j
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserService userService;

    @Autowired
    private OkrService okrService;

    @Autowired
    private KeyResultService keyResultService;

    @GetMapping("/{id}")
    @ApiOperation("获取organization详细信息")
    public OrganizationDTO getOrganization(@PathVariable("id") @NotNull Long organizationId) {
        return this.organizationService.getOrganizationById(organizationId);
    }

    @PostMapping
    @ApiOperation("添加organization")
    public Long insertOrganization(@RequestBody @Valid InsertOrganizationDTO insertOrganizationDTO) {
        return this.organizationService.insertOrganization(insertOrganizationDTO);
    }


    @PutMapping
    @ApiOperation("修改organization属性")
    public void updateOrganization(@RequestBody @Valid UpdateOrganizationDTO updateOrganizationDTO) {
        this.organizationService.updateOrganization(updateOrganizationDTO);
    }


    @PostMapping("/{id}/default")
    @ApiOperation("设置用户的默认公司")
    public void setUserDefaultOrganization(@PathVariable("id") @NotNull Long organizationId) {
        DefaultOrgnazationSettingDTO dto = new DefaultOrgnazationSettingDTO();
        dto.setOrganizationId(organizationId);
        userService.setUserDefaultOrganization(dto);
    }

    @GetMapping("/list")
    @ApiOperation("获取用户的组织列表")
    public List<OrganizationDTO> listUserOrganization() {
        List<OrganizationDTO> list = organizationService.listOrganizationsWithDefaultSettings();
        if(CollectionUtils.isEmpty(list)){
            initDemoData();
            list = organizationService.listOrganizationsWithDefaultSettings();
        }
        return list;
    }

    @GetMapping("/{id}/invitation_link")
    @ApiOperation("获取组织的邀请链接")
    public String getOrganizationInvitationLink(@PathVariable("id") @NotNull Long organizationId) {
        return this.memberService.generateOrganizationInvitationLink(organizationId);
    }

    @GetMapping("/{id}/members")
    @ApiOperation("获取organzation的member信息")
    public List<MemberDTO> listOrganizationMembers(@PathVariable("id") @NotNull Long organizationId) {
        return this.memberService.listOrganizationMembers(organizationId, false);
    }

    @GetMapping("test")
    @ApiOperation("获取organzation的member信息")
    public void  test() {
        initDemoData();
    }

    private void initDemoData() {
        UserInfoDTO userInfo = userService.getCurrentUserInfo();
        //初始化组织
        InsertOrganizationDTO organizationInsert=new InsertOrganizationDTO();
        organizationInsert.setName("2OKR");
        organizationInsert.setDesc("OKR Demo");
        long orgId = organizationService.insertOrganization(organizationInsert);
        
        //初始化okr
        InsertObjectiveDTO insertObjective =new InsertObjectiveDTO();
        insertObjective.setName("提升2OKR目标管理工具的使用性。");
        insertObjective.setDesc("提升2OKR目标管理工具的使用性,帮助企业聚焦目标,提高企业的管理效率。");
        Date now =new Date();
        insertObjective.setOrganizationId(orgId);
        insertObjective.setOwnerId(userInfo.getId());
        insertObjective.setLevel(ObjectiveLevelEnum.ORGANIZATION);
        insertObjective.setPeriodStartTime(DateUtil.getQuarterStartTime(now));
        insertObjective.setPeriodEndTime(DateUtil.getQuarterEndTime(now));
        insertObjective.setValidatorId(userInfo.getId());
        long objectiveId = okrService.insertObjective(insertObjective);

        InsertObjectiveKeyResultDTO keyResult1=new InsertObjectiveKeyResultDTO();
        keyResult1.setName("2OKR访问速度提高5%");
        keyResult1.setDesc("提高2OKR客户端的访问性能");
        keyResult1.setObjectiveId(objectiveId);
        keyResult1.setMetricStartValue(100.00);
        keyResult1.setMetricEndValue(95.00);
        keyResult1.setMetricUnit("7");
        keyResult1.setConfidenceLevel(80);
        keyResult1.setOwnerId(userInfo.getId());
        keyResult1.setWeight(1.00);
        Long krId1=keyResultService.insertObjectiveKeyResult(keyResult1);
        UpdateObjectiveKeyResulProgressDTO updateKeyResultProgress=new UpdateObjectiveKeyResulProgressDTO();
        updateKeyResultProgress.setId(krId1);
        updateKeyResultProgress.setProgress(0.6);
        keyResultService.updateObjectiveKeyResultProgress(updateKeyResultProgress);

        InsertObjectiveKeyResultDTO keyResult2=new InsertObjectiveKeyResultDTO();
        keyResult2.setName("退出2OKR工具web端和微信小程序客户端");
        keyResult2.setDesc("提供多样化的工具,提高2OKR的便利性");
        keyResult2.setObjectiveId(objectiveId);
        keyResult2.setMetricStartValue(0.0);
        keyResult2.setMetricEndValue(2.0);
        keyResult2.setMetricUnit("7");
        keyResult2.setConfidenceLevel(90);
        keyResult2.setOwnerId(userInfo.getId());
        keyResult2.setWeight(1.00);
        Long krId2=keyResultService.insertObjectiveKeyResult(keyResult2);
        UpdateObjectiveKeyResulProgressDTO updateKeyResultProgress2=new UpdateObjectiveKeyResulProgressDTO();
        updateKeyResultProgress2.setId(krId2);
        updateKeyResultProgress2.setProgress(0.7);
        keyResultService.updateObjectiveKeyResultProgress(updateKeyResultProgress2);

    }
    


}
