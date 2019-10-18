package com.coreteam.okr.dto.dictionary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: DictDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 15:24
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DictDTO {

    private Long id;
    /**
     * the type of dictionary
     */
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
