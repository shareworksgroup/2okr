package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.Organization;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * OrganizationDAO继承基类
 */
@Mapper
public interface OrganizationDAO extends MyBatisBaseDao<Organization, Long> {
    List<Organization> listOrganizationsByUser(Long userId);
}