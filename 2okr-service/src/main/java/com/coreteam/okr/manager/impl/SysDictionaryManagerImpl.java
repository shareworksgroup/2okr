package com.coreteam.okr.manager.impl;

import com.coreteam.okr.dao.SysDictionaryDAO;
import com.coreteam.okr.dto.dictionary.DictionaryDTO;
import com.coreteam.okr.dto.dictionary.DictionaryItemDTO;
import com.coreteam.okr.entity.SysDictionary;
import com.coreteam.okr.manager.SysDictionaryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @ClassName: SysDictionaryManagerImpl
 * @Description implment dictionary service
 * @Author jianyong.jiang
 * @Date 2019/3/25 9:57
 * @Version 1.0.0
 */
@Service
public class SysDictionaryManagerImpl implements SysDictionaryManager {
    private static final Map<String, DictionaryDTO> dictionaryMap = new HashMap<>();

    @Autowired
    private SysDictionaryDAO dao;

    @Override
    public void init() {
        List<SysDictionary> list = this.dao.selectAll();
        list.stream().collect(groupingBy(SysDictionary::getDictType)).entrySet().forEach(item -> {
            List<DictionaryItemDTO> itemDTOList = item.getValue().stream().map(d -> new DictionaryItemDTO(
                    d.getDictName(), d.getDictValue(), d.getRemark())).collect(Collectors.toList());
            dictionaryMap.put(item.getKey(), new DictionaryDTO(item.getKey(), itemDTOList));
        });
    }

    @Override
    public DictionaryDTO findByType(String type) {
        return dictionaryMap.get(type);
    }

    @Override
    public Map<String, DictionaryDTO> listDictionaries() {
        return Collections.unmodifiableMap(dictionaryMap);
    }
}
