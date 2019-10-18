package com.coreteam.okr.web.controller;

import com.coreteam.okr.aop.SubscriptionActiveLimit;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveStatisticsDTO;
import com.coreteam.okr.dto.organization.InsertOrganizationDTO;
import com.coreteam.okr.dto.organization.OrganizationDTO;
import com.coreteam.okr.dto.organization.UpdateOrganizationDTO;
import com.coreteam.okr.dto.team.TeamStatisticsDTO;
import com.coreteam.okr.service.MemberService;
import com.coreteam.okr.service.OkrService;
import com.coreteam.okr.service.OrganizationService;
import com.coreteam.okr.service.TeamService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: OrganizationController
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 9:12
 * @Version 1.0.0
 */
@RestController
@RequestMapping("organizations")
@AuditLogAnnotation(value = "organization接口")
@Slf4j
public class OrganizationController {

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

    @DeleteMapping("/{id}")
    @ApiOperation("删除organization")
    public void deleteOrganization(@PathVariable("id") @NotNull Long organizationId) {
        this.organizationService.deleteOrganization(organizationId);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取organization详细信息")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public OrganizationDTO getOrganization(@PathVariable("id") @NotNull Long organizationId) {
        return this.organizationService.getOrganizationById(organizationId);
    }

    @GetMapping("/{id}/teams")
    @ApiOperation("获取organzation的team信息")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public List<TeamStatisticsDTO> listTeams(@PathVariable("id") @NotNull Long organizationId) {
        return this.teamService.listTeamsWithStatisticsByOrganizationFilter(organizationId);
    }

    @GetMapping("/{id}/teams/invitable")
    @ApiOperation("获取organzation中用户添加成员的team列表")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public List<TeamStatisticsDTO> listTeamsInvitable(@PathVariable("id") @NotNull Long organizationId) {
        return this.teamService.listTeamsWithStatisticsInvitableByOrganizationFilter(organizationId);
    }


    // 业务调整过期的api无需再使用，后续将删除
    @GetMapping("/{id}/okr")
    @ApiOperation("获取organizaiton的okr信息")
    @Deprecated
    public PageInfo<ObjectiveDTO> listOrganizationLevelObjectiveKeyResult(@PathVariable("id") @NotNull Long organizationId, Integer pageNumber, Integer pageSize, Date start, Date end) {
        return organizationService.listOrganizationLevelObjectives(organizationId, pageNumber, pageSize, start, end);
    }

    @GetMapping("/{id}/okr/statistics")
    @ApiOperation("获取organizaiton的okr统计信息")
    @Deprecated
    public ObjectiveStatisticsDTO getOrganizationLevelObjectiveStatistics(@PathVariable("id") @NotNull Long organizationId, Date start, Date end) {
        return this.organizationService.getOrganizationLevelObjectiveStatistics(organizationId, start, end);
    }


    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OkrService okrService;
}
