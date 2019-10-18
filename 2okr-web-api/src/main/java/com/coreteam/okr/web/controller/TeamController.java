package com.coreteam.okr.web.controller;

import com.coreteam.okr.aop.SubscriptionActiveLimit;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveStatisticsDTO;
import com.coreteam.okr.dto.team.InsertTeamDTO;
import com.coreteam.okr.dto.team.TeamDTO;
import com.coreteam.okr.dto.team.UpdateTeamDTO;
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

/**
 * @ClassName: TeamController
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 14:36
 * @Version 1.0.0
 */
@RestController
@RequestMapping("teams")
@AuditLogAnnotation(value = "team接口")
@Slf4j
public class TeamController {

    @PostMapping
    @ApiOperation("添加team")
    @SubscriptionActiveLimit(organizationId = "#{insertTeamDTO.organizationId}")
    public Long insertTeam(@RequestBody @Valid InsertTeamDTO insertTeamDTO) {
        return this.service.insertTeam(insertTeamDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改team")
    public void updateTeam(@PathVariable("id") @NotNull Long id,@RequestBody @Valid UpdateTeamDTO updateTeamDTO) {
        this.service.updateTeam(id,updateTeamDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除team")
    public void deleteTeam(@PathVariable("id") @NotNull Long id) {
        this.service.deleteTeam(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取team")
    public TeamDTO getTeam(@PathVariable("id") @NotNull Long id) {
        return this.service.getTeam(id);
    }

    // 业务调整过期的api无需再使用，后续将删除
    @GetMapping("/{id}/okr")
    @ApiOperation("获取team的okr信息")
    @Deprecated
    public PageInfo<ObjectiveDTO> listTeamLevelObjectiveKeyResult(@PathVariable("id") @NotNull Long tearmId, Integer pageNumber, Integer pageSize, Date start, Date end) {
        return service.listTeamLevelObjectives(tearmId,pageNumber,pageSize,start,end);
    }

    @GetMapping("/{id}/okr/statistics")
    @ApiOperation("获取team的okr统计信息")
    @Deprecated
    public ObjectiveStatisticsDTO getTeamLevelObjectiveStatistics(@PathVariable("id") @NotNull Long teamId, Date start, Date end){
        return  this.service.getTeamLevelObjectiveStatistics(teamId,start,end);
    }

    @Autowired
    private TeamService service;

}
