package com.coreteam.okr.dto.dictionary;

/**
 * @ClassName: DictionaryItemDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/25 9:38
 * @Version 1.0.0
 */
public class DictionaryItemDTO {
    private String itemName;
    private String itemValue;
    private String remark;

    public DictionaryItemDTO(String name, String value, String remark) {
        this.itemName = name;
        this.itemValue = value;
        this.remark = remark;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
