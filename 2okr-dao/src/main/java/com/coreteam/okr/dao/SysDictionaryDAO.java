package com.coreteam.okr.dao;

import com.coreteam.core.data.MyBatisBaseDao;
import com.coreteam.okr.entity.SysDictionary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * SysDictionaryDAO继承基类
 */
@Mapper
public interface SysDictionaryDAO extends MyBatisBaseDao<SysDictionary, Long> {
    List<SysDictionary> selectAll();

    void deleteByDictionaryType(String type);

    List<SysDictionary> listDictByType(String dictType);

}