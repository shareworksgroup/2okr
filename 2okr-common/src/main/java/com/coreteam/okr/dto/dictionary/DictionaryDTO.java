package com.coreteam.okr.dto.dictionary;

import java.util.List;

/**
 * @ClassName: DictionaryDTO
 * @Description Represents a dictionary dto
 * @Author jianyong.jiang
 * @Date 2019/3/25 9:36
 * @Version 1.0.0
 */
public class DictionaryDTO {
    private String type;
    private List<DictionaryItemDTO> itemList;

    public DictionaryDTO(String type, List<DictionaryItemDTO> itemList) {
        this.type = type;
        this.itemList = itemList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DictionaryItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<DictionaryItemDTO> itemList) {
        this.itemList = itemList;
    }
}

