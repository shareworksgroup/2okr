package com.coreteam.okr.web.controller;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.okr.dao.OrganizationDAO;
import com.coreteam.okr.dto.plantemplate.SaveWeeklyPlanTemplateDTO;
import com.coreteam.okr.dto.plantemplate.WeeklyPlanTemplateDTO;
import com.coreteam.okr.entity.Organization;
import com.coreteam.okr.service.UserService;
import com.coreteam.okr.service.WeeklyPlanTemplateService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("weeklyplantemplates")
@AuditLogAnnotation(value = "weeklyplantemplates接口")
@Slf4j
public class WeekPlanTemplateController {
    @Autowired
    private WeeklyPlanTemplateService weeklyPlanTemplateService;

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private UserService userService;

    @GetMapping("/default")
    @ApiOperation("获取默认模板信息")
    public WeeklyPlanTemplateDTO getDefaultTemplate(){
        return weeklyPlanTemplateService.getDefaultTemplate();
    }

    @GetMapping("/{orgId}")
    @ApiOperation("通过id获取模板信息")
    public WeeklyPlanTemplateDTO getWeeklyPlanTemplateByOrganization(@PathVariable("orgId") @NotNull Long id){
        Organization org = this.organizationDAO.selectByPrimaryKey(id);
        if(org==null){
            throw new CustomerException("the organization not exsit");
        }
        WeeklyPlanTemplateDTO dto= weeklyPlanTemplateService.getWeeklyPlanTemplateByOrganization(id);
        if(dto==null){
            weeklyPlanTemplateService.setOrganizationDefaultWeeklyPlanTemplate(org);
            dto= weeklyPlanTemplateService.getWeeklyPlanTemplateByOrganization(id);
        }
        return dto;
    }

    @PostMapping("")
    @ApiOperation("save模板信息")
    public void saveWeeklyPlanTemplate(@RequestBody @Valid SaveWeeklyPlanTemplateDTO saveWeeklyPlanTemplateDTO){
        weeklyPlanTemplateService.saveWeeklyPlanTemplate(saveWeeklyPlanTemplateDTO);
    }

    @DeleteMapping("/organization/{id}")
    @ApiOperation("删除用户周计划item")
    public void deleteWeeklyPlanTemplateItem(@PathVariable("id") @NotNull Long organizationId,Long itemId){
        weeklyPlanTemplateService.deleteWeeklyPlanTemplateItem(organizationId,itemId);
    }
}
