package com.coreteam.okr.service.impl;

import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.dao.SysDictionaryDAO;
import com.coreteam.okr.dto.dictionary.DictDTO;
import com.coreteam.okr.dto.dictionary.InsertDictDTO;
import com.coreteam.okr.entity.SysDictionary;
import com.coreteam.okr.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: DictServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 15:37
 * @Version 1.0.0
 */
@Service
public class DictServiceImpl implements DictService {
    @Override
    public List<DictDTO> listDict(String dictType) {
        return BeanConvertUtils.batchTransform(DictDTO.class, this.sysDictionaryDAO.listDictByType(dictType));
    }

    @Override
    @Transactional
    public Long insertDict(InsertDictDTO insertDictDTO) {
        SysDictionary dict = new SysDictionary();
        BeanConvertUtils.copyEntityProperties(insertDictDTO, dict);
        dict.initializeForInsert();
        this.sysDictionaryDAO.insertSelective(dict);
        return dict.getId();
    }

    @Override
    @Transactional
    public void updateDict(Long id, InsertDictDTO updateDictDTO) {
        SysDictionary dict = new SysDictionary();
        BeanConvertUtils.copyEntityProperties(updateDictDTO, dict);
        dict.setId(id);
        dict.initializeForUpdate();
        this.sysDictionaryDAO.updateByPrimaryKeySelective(dict);
    }

    @Override
    @Transactional
    public void deleteDict(Long id) {
        this.sysDictionaryDAO.deleteByPrimaryKey(id);
    }

    @Autowired
    private SysDictionaryDAO sysDictionaryDAO;
}
