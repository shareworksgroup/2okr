package com.coreteam.okr.manager;

import com.coreteam.okr.dto.dictionary.DictionaryDTO;

import java.util.Map;

/**
 * @ClassName: SysDictionaryManager
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/22 15:32
 * @Version 1.0.0
 */
public interface SysDictionaryManager {

    /**
     * 　* @description: get system all dictionary list
     *
     */
    Map<String, DictionaryDTO> listDictionaries();

    /**
     * 　* @description: initialize the dictionary service
     */
    void init();

    /**
     * 　* @description: find a dictionary by dictionary type
     *
     * @param type dictionary type
     * @date 2019/3/25 9:35
     */
    DictionaryDTO findByType(String type);
}
