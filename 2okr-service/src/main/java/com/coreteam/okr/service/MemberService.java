package com.coreteam.okr.service;

import com.coreteam.okr.dto.invite.InviteMemberDTO;
import com.coreteam.okr.dto.member.*;
import com.coreteam.okr.dto.user.SimpleUserInfoDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: MemberService
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/22 14:50
 * @Version 1.0.0
 */
public interface MemberService {
    /**
     * 邀请添加member
     * @param inviteList
     */
    void inviteMembers(InviteMemberDTO inviteList);


    /**
     *添加被邀请的人员
     * @param invitedCipher
     */
    void acceptInvitationAndupdateMember(String invitedCipher);


    /**
     * 添加成员
     * @param insertMemberDTO
     * @return
     */
    long insertMember(InsertMemberDTO insertMemberDTO, Boolean isOwner, Boolean ignorePermission);


    /**
     * 添加成员，需要邮件邀请确认
     * @param dto
     */
    Long insertMember(InsertMemberNeedInviteDTO dto, Boolean isOwner, Boolean ignorePermission);

    /**
     * 移除member
     * @param id
     * @return
     */
    boolean deleteMember(Long id);


    /**
     * change org 用户的role
     * @param changeOrgMemberRoleDTO
     */
    void changeOrgMemberRoleDTO(ChangeOrgMemberRoleDTO changeOrgMemberRoleDTO);

    /**
     * change team 的role
     * @param changeTeamMemberRoleDTO
     */
    void changeTeamMemberRoleDTO(ChangeTeamMemberRoleDTO changeTeamMemberRoleDTO);

    /**
     *
     * @param teamId
     * @return
     */
    List<MemberStatisticsDTO> listTeamMemberWithStatistics(Long teamId);

    /**
     *
     * @param organizationId
     * @return
     */
    List<MemberStatisticsDTO> listOrganizationMemberWithStatistics(Long organizationId);

    List<MemberDTO> listOrganizationMembers(Long organizationId, Boolean onlyOwner);

    List<MemberDTO> listTeamMembers(Long teamId, Boolean onlyOwner);

    List<SimpleUserInfoDTO> listOrganizationMemberConverToUser(Long organizationId);

    List<SimpleUserInfoDTO> listTeamMemberConverToUser(Long teamId);

    Integer countOrganizationMember(Long id);

    /**
     * 分页获取组织的成员
     * @param organizationId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageInfo<MemberDTO> listOrganizationMembersByPage(Long organizationId, Integer pageNumber, Integer pageSize);

    /**
     * 分页获取team的成员
     * @param teamId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageInfo<MemberDTO> listTeamMembersByPage(Long teamId, Integer pageNumber, Integer pageSize);

    /**
     * 分页获取组织成员，包含统计信息
     * @param organizationId
     * @param pageNumber
     * @param pageSize
     * @param name
     * @return
     */
    PageInfo<MemberStatisticsDTO> listOrganizationMemberWithStatisticsByPage(Long organizationId, Integer pageNumber, Integer pageSize, String name);

    /**
     * 分页获取team成员，包含统计信息
     * @param teamId
     * @param pageNumber
     * @param pageSize
     * @param name
     * @return
     */
    PageInfo<MemberStatisticsDTO> listTeamMemberWithStatisticsByPage(Long teamId, Integer pageNumber, Integer pageSize, String name);

    /**
     * 获取用户所在组织的成员列表
     * @param user
     * @return
     */
    List<MemberSimpleInfoDTO> listPartnersCorssOrganization(UserInfoDTO user);


    /**
     * 生成组织的邀请链接
     * @param organizationId
     * @return
     */
    String generateOrganizationInvitationLink(Long organizationId);

    /**
     * 生成团队的邀请链接
     * @param organizationId
     * @return
     */
    String generateTeamInvitationLink(Long organizationId);

    String joinTOGroupByPublicINvitationLink(String info);

    InvitationInfoDTO decryptInvitationInfo(String info);

    /**
     *  获取用户常规weekly plan reciver
     * @param organizationId
     * @param id
     * @return
     */
    List<SimpleUserInfoDTO> listUsualWeeklyPlanReportReciver(Long organizationId, Long id);

    /**
     * 获取组织的
     * @param id
     * @return
     */
    Map<String, List<Long>> listOrganizationTeamMembers(Long id);

}
