package com.coreteam.okr.dto.dictionary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: InsertDictDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 15:31
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertDictDTO {
    @NotNull
    private String dictType;

    /**
     * dictionary item name
     */
    @NotNull
    private String dictName;

    /**
     * dictionary item value
     */
    @NotNull
    private String dictValue;

    /**
     * dictionary item display order
     */

    private Short sort;

    /**
     * remark for dictionary item
     */
    private String remark;
}
