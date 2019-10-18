package com.coreteam.okr.wx.controller;

import com.coreteam.okr.dto.invite.InviteMemberDTO;
import com.coreteam.okr.dto.member.InvitationInfoDTO;
import com.coreteam.okr.dto.member.MemberInvitedDTO;
import com.coreteam.okr.service.MemberService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: MemberController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/06 11:05
 * @Version 1.0.0
 */

@RestController
@RequestMapping("members")
@AuditLogAnnotation(value = "member接口")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/invite")
    @ApiOperation("邀请添加成员")
    public void inviteTeamMembers(@RequestBody @Valid InviteMemberDTO inviteList) {
        this.memberService.inviteMembers(inviteList);
    }

    @DeleteMapping("{id}")
    @ApiOperation("移除member")
    public void deleteMember(@PathVariable("id") @NotNull Long id) {
        this.memberService.deleteMember(id);
    }

    @PostMapping("/invite/public_link/accept")
    @ApiOperation("接受邀请")
    public String acceptInviteByPublicLink(@RequestBody @Valid MemberInvitedDTO dto) {
        return this.memberService.joinTOGroupByPublicINvitationLink(dto.getInfo());
    }

    @PostMapping("/invite/public_link/info")
    @ApiOperation("解码获取邀请信息")
    public InvitationInfoDTO decryptInvitationInfo(@RequestBody @Valid MemberInvitedDTO dto) {
        return this.memberService.decryptInvitationInfo(dto.getInfo());
    }



}
