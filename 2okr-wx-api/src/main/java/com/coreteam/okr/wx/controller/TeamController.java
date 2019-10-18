package com.coreteam.okr.wx.controller;

import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.team.*;
import com.coreteam.okr.service.MemberService;
import com.coreteam.okr.service.TeamService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: TeamController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/06 10:54
 * @Version 1.0.0
 */

@RestController
@RequestMapping("teams")
@AuditLogAnnotation(value = "teams weixin api 接口")
@Slf4j
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/list")
    @ApiOperation("获取组织的team列表")
    public List<TeamStatisticsWithMembersDTO> listTeams(Long organizationId) {
        this.teamService.listTeamsWithStatisticsByOrganizationFilter(organizationId);
        return this.teamService.listTeamsWithStatisticsAndMembersByOrganizationFilter(organizationId);
    }


    @PostMapping
    @ApiOperation("添加team")
    public Long insertTeam(@RequestBody @Valid InsertTeamDTO insertTeamDTO) {
        return this.teamService.insertTeam(insertTeamDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改team")
    public void updateTeam(@PathVariable("id") @NotNull Long id, @RequestBody @Valid UpdateTeamDTO updateTeamDTO) {
        this.teamService.updateTeam(id, updateTeamDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取team")
    public TeamDTO getTeam(@PathVariable("id") @NotNull Long id) {
        return this.teamService.getTeam(id);
    }

    @GetMapping("/{id}/invitation_link")
    @ApiOperation("获取team的邀请链接")
    public String getTeamInvitationLink(Long teamId) {
        return this.memberService.generateTeamInvitationLink(teamId);
    }


    @GetMapping("/{id}/members")
    @ApiOperation("获取team下的members")
    public List<MemberDTO> listTeamOwner(@PathVariable("id") @NotNull Long teamId) {
        return this.memberService.listTeamMembers(teamId, false);
    }


}
