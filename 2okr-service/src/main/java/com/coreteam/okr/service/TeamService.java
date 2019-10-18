package com.coreteam.okr.service;

import com.coreteam.okr.constant.PrivilegeTypeEnum;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.dto.objective.ObjectiveStatisticsDTO;
import com.coreteam.okr.dto.team.*;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author jianyong.jiang
 * @date 2019/03/18
 */
public interface TeamService {
    /**
     * 新增team，如果有指定member id，则添加对应的member
     * @param insertTeamDTO
     * @return
     */
    Long insertTeam(InsertTeamDTO insertTeamDTO);

    /**
     * update
     *
     * @param id
     * @param updateTeamDTO
     * @return
     */
    boolean updateTeam(Long id, UpdateTeamDTO updateTeamDTO);

    boolean deleteTeam(Long id);

    TeamDTO getTeam(Long id);


    List<TeamStatisticsDTO> listTeamsWithStatisticsByOrganizationFilter(Long organizationId);

    /**
     * 获取组织中当前用户能够添加成员的team列表
     * @param organizationId
     * @return
     */
    List<TeamStatisticsDTO> listTeamsWithStatisticsInvitableByOrganizationFilter(Long organizationId);

    List<TeamStatisticsWithMembersDTO> listTeamsWithStatisticsAndMembersByOrganizationFilter(Long organizationId);

    /**
     * 获取 okr
     * @param tearmId
     * @param pageNumber
     * @param pageSize
     * @param start
     * @param end
     * @return
     */
    PageInfo<ObjectiveDTO> listTeamLevelObjectives(Long tearmId, Integer pageNumber, Integer pageSize, Date start, Date end);

    /**
     *
     * get okr 统计信息
     * @param teamId
     * @param start
     * @param end
     * @return
     */
    ObjectiveStatisticsDTO getTeamLevelObjectiveStatistics(Long teamId, Date start, Date end);

    /**
     *
     * @param teamId
     * @param type
     * @return
     */
    Boolean hasPermission(Long teamId, PrivilegeTypeEnum type);


}
