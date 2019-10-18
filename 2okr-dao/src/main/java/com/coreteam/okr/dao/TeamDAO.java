package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.dto.team.TeamStatisticsDTO;
import com.coreteam.okr.entity.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TeamDAO继承基类
 */
@Mapper
public interface TeamDAO extends MyBatisBaseDao<Team, Long> {
    /**
     * 查询org的team信息
     * @param organizationId
     * @return
     */
    List<TeamStatisticsDTO> listTeamsStatisticsByOrganization(Long organizationId);

    List<Team> listTeamsByUserId(@Param("organizationId") Long organizationId, @Param("userId") Long id);
}