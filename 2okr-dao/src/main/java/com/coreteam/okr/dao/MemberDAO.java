package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.constant.MemberRoleEnum;
import com.coreteam.okr.dto.member.MemberSimpleInfoDTO;
import com.coreteam.okr.dto.member.MemberStatisticsDTO;
import com.coreteam.okr.dto.user.SimpleUserInfoDTO;
import com.coreteam.okr.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MemberDAO继承基类
 */
@Mapper
public interface MemberDAO extends MyBatisBaseDao<Member, Long> {
    Member findTeamMember(@Param("teamId") Long teamId, @Param("userId") Long userId);

    Member findOrganizationMember(@Param("organizationId") Long organizationId, @Param("userId") Long userId);

    Member findTeamMemberIgnoreStatus(@Param("teamId") Long teamId, @Param("userId") Long userId);

    Member findOrganizationMemberIgnoreStatus(@Param("organizationId") Long organizationId, @Param("userId") Long userId);

    Member findTeamMemberByEmailBeforeRegister(@Param("teamId") Long entityId, @Param("email") String email);

    Member findOrganizationMemberByEmailBeforeRegister(@Param("organizationId") Long entityId, @Param("email") String email);


    int updateTeamMemberRole(@Param("teamId") Long teamId, @Param("userId") Long userId, @Param("role") MemberRoleEnum role);

    int updateOrganizationMemberRole(@Param("organizationId") Long organizationId, @Param("userId") Long userId, @Param("role") MemberRoleEnum role);

    void deleteAll();

    Integer countOrganizationMember(Long id);

    List<MemberStatisticsDTO> listTeamMemberStatistics(@Param("teamId") Long teamId, @Param("name") String nameQuery);

    List<MemberStatisticsDTO> listOrganizationMemberStatistics(@Param("organizationId") Long organizationId, @Param("name") String nameQuery);

    List<Member> listOrganizationMembers(@Param("organizationId") Long organizationId, @Param("onlyOwner") Boolean onlyOwner);

    List<Member> listTeamMembers(@Param("teamId") Long teamId, @Param("onlyOwner") Boolean onlyOwner);

    Member findMemberByInvitedId(Long id);

    List<MemberSimpleInfoDTO> listAllMembersByUser(@Param("userId") Long id);

    void updateMemberUserName(@Param("userId") Long userId, @Param("userName") String userName);

    List<SimpleUserInfoDTO> listUser(@Param("entityId") Long entityId, @Param("type") String type);

    List<Member> findOrgAllTeamMembers(Long id);
}