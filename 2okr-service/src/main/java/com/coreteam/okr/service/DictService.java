package com.coreteam.okr.service;

import com.coreteam.okr.dto.dictionary.DictDTO;
import com.coreteam.okr.dto.dictionary.InsertDictDTO;

import java.util.List;

/**
 * @ClassName: DictService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 15:28
 * @Version 1.0.0
 */
public interface DictService {
    List<DictDTO> listDict(String dictType);

    Long insertDict(InsertDictDTO insertDictDTO);

    void updateDict(Long id, InsertDictDTO updateDictDTO);

    void deleteDict(Long id);
}
