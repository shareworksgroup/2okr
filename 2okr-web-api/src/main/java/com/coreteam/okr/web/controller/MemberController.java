package com.coreteam.okr.web.controller;

import com.coreteam.okr.aop.SubscriptionActiveLimit;
import com.coreteam.okr.dto.invite.InviteMemberDTO;
import com.coreteam.okr.dto.member.*;
import com.coreteam.okr.dto.user.SimpleUserInfoDTO;
import com.coreteam.okr.service.InviteService;
import com.coreteam.okr.service.MemberService;
import com.coreteam.okr.service.UserService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: MemberController
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/28 17:20
 * @Version 1.0.0
 */
@RestController
@RequestMapping("members")
@AuditLogAnnotation(value = "member接口")
@Slf4j
public class MemberController {

    /*========================================成员邀请 ================================================*/

    @PostMapping("/invite")
    @ApiOperation("邀请添加成员")
    public void inviteTeamMembers(@RequestBody @Valid InviteMemberDTO inviteList) {
        this.memberService.inviteMembers(inviteList);
    }

    @PostMapping("/invite/{id}/resend")
    @ApiOperation("重新发送邀请邮件")
    public void reSendInvitedMessage(@PathVariable("id") @NotNull Long invitedId) {
        this.inviteService.reSendInvitedMessage(invitedId);
    }

    @PostMapping
    @ApiOperation("直接添加成员")
    public Long insertMember(@RequestBody @Valid InsertMemberDTO insertMemberDTO) {
        InsertMemberNeedInviteDTO dto = new InsertMemberNeedInviteDTO(insertMemberDTO.getOrganizationId(), insertMemberDTO.getTeamId(), userService.getUserInfoById(insertMemberDTO.getUserId()).getEmail(), insertMemberDTO.getType());
        return this.memberService.insertMember(dto, false,true);
    }

    @GetMapping("/organization/{id}/invitation_link")
    @ApiOperation("获取组织的邀请链接")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public String generateOrganizationInvitationLink(@PathVariable("id") @NotNull Long organizationId) {
        return this.memberService.generateOrganizationInvitationLink(organizationId);
    }

    @GetMapping("/team/{id}/invitation_link")
    @ApiOperation("获取team的邀请链接")
    public String generateTeamInvitationLink(@PathVariable("id") @NotNull Long teamId) {
        return this.memberService.generateTeamInvitationLink(teamId);
    }


    /*========================================接受邀请 ================================================*/
    @PostMapping("/invite/accept")
    @ApiOperation("接受邀请")
    public void acceptInvited(@RequestBody @Valid MemberInvitedDTO dto) {
        this.memberService.acceptInvitationAndupdateMember(dto.getInfo());
    }

    @PostMapping("/invite/public_link/accept")
    @ApiOperation("接受邀请")
    public String joinToGroupByPublicLink(@RequestBody @Valid MemberInvitedDTO dto) {
        return this.memberService.joinTOGroupByPublicINvitationLink(dto.getInfo());
    }

    /*========================================成员状态operater ================================================*/


    @PutMapping("/organzation/role")
    @ApiOperation("改变organization member的角色")
    @SubscriptionActiveLimit(organizationId = "#{changeOrgMemberRoleDTO.organizationId}")
    public void updateMember(@RequestBody @Valid ChangeOrgMemberRoleDTO changeOrgMemberRoleDTO) {
        this.memberService.changeOrgMemberRoleDTO(changeOrgMemberRoleDTO);
    }

    @PutMapping("/team/role")
    @ApiOperation("改变 team member的角色")
    public void updateMember(@RequestBody @Valid ChangeTeamMemberRoleDTO changeTeamMemberRoleDTO) {
        this.memberService.changeTeamMemberRoleDTO(changeTeamMemberRoleDTO);
    }

    @DeleteMapping("{id}")
    @ApiOperation("移除member")
    public void deleteMember(@PathVariable("id") @NotNull Long id) {
        this.memberService.deleteMember(id);
    }

    /*========================================成员查询接口 ================================================*/



    @GetMapping("/organization/{id}")
    @ApiOperation("获取organzation的member信息")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public List<MemberDTO> listOrganizationMembers(@PathVariable("id") @NotNull Long organizationId) {
        return this.memberService.listOrganizationMembers(organizationId, false);
    }

    @GetMapping("/team/{id}")
    @ApiOperation("获取team下的member信息")
    public List<MemberDTO> listTeamMembers(@PathVariable("id") @NotNull Long teamId) {
        return this.memberService.listTeamMembers(teamId, false);
    }

    @GetMapping("/organization/{id}/page")
    @ApiOperation("分页获取organzation的member信息")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public PageInfo<MemberDTO> listOrganizationMembersByPage(@PathVariable("id") @NotNull Long organizationId, Integer pageNumber, Integer pageSize) {
        return this.memberService.listOrganizationMembersByPage(organizationId, pageNumber, pageSize);
    }

    @GetMapping("/team/{id}/page")
    @ApiOperation("分页获取team下的member信息")
    public PageInfo<MemberDTO> listTeamMembersByPage(@PathVariable("id") @NotNull Long teamId, Integer pageNumber, Integer pageSize) {
        return this.memberService.listTeamMembersByPage(teamId, pageNumber, pageSize);
    }





    @GetMapping("/organization/{id}/user")
    @ApiOperation("获取organzation的user信息")
    public List<SimpleUserInfoDTO> listOrganizationUser(@PathVariable("id") @NotNull Long organizationId) {
        return this.memberService.listOrganizationMemberConverToUser(organizationId);
    }


    @GetMapping("/team/{id}/user")
    @ApiOperation("获取team下的user信息")
    public List<SimpleUserInfoDTO> listTeamUser(@PathVariable("id") @NotNull Long teamId) {
        return this.memberService.listTeamMemberConverToUser(teamId);
    }

    @GetMapping("/team/{id}/statistics")
    @ApiOperation("获取team下的member信息，包括人员统计")
    public List<MemberStatisticsDTO> listTeamMemberStatistics(@PathVariable("id") @NotNull Long teamId) {
        return this.memberService.listTeamMemberWithStatistics(teamId);
    }


    @GetMapping("/team/{id}/statistics/page")
    @ApiOperation("分页获取team下的member信息，包括人员统计")
    public PageInfo<MemberStatisticsDTO> listTeamMemberStatisticsByPage(@PathVariable("id") @NotNull Long teamId, Integer pageNumber, Integer pageSize, @RequestParam(required = false) String name) {
        return this.memberService.listTeamMemberWithStatisticsByPage(teamId, pageNumber, pageSize, name);
    }


    //业务调整，接口将不再使用，后续将删除
    @GetMapping("/organization/{id}/statistics/page")
    @ApiOperation("分页获取organzation的member信息,包括人员统计")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public PageInfo<MemberStatisticsDTO> listOrganizationMemberStatisticsByPage(@PathVariable("id") @NotNull Long organizationId, Integer pageNumber, Integer pageSize, @RequestParam(required = false) String name) {
        return this.memberService.listOrganizationMemberWithStatisticsByPage(organizationId, pageNumber, pageSize, name);
    }

    @GetMapping("/organization/{id}/statistics")
    @ApiOperation("获取organzation的member信息,包括人员统计")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public List<MemberStatisticsDTO> listOrganizationMemberStatistics(@PathVariable("id") @NotNull Long organizationId) {
        return this.memberService.listOrganizationMemberWithStatistics(organizationId);
    }

    @GetMapping("/organization/{id}/owner")
    @ApiOperation("获取organzation的ownber信息")
    @SubscriptionActiveLimit(organizationId = "#{organizationId}")
    public List<MemberDTO> listOrganizationOwner(@PathVariable("id") @NotNull Long organizationId) {
        return this.memberService.listOrganizationMembers(organizationId, true);
    }

    @GetMapping("/team/{id}/owner")
    @ApiOperation("获取team下的owner信息")
    public List<MemberDTO> listTeamOwner(@PathVariable("id") @NotNull Long teamId) {
        return this.memberService.listTeamMembers(teamId, true);
    }


    @Autowired
    private MemberService memberService;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private UserService userService;

}
