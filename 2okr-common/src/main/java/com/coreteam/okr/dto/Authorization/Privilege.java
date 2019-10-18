package com.coreteam.okr.dto.Authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: Privilege
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/29 9:58
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Privilege {
    private Boolean addable;
    private Boolean viewable;
    private Boolean updatable;
    private Boolean deletable;

    public Privilege() {
        this.addable = false;
        this.viewable = false;
        this.updatable = false;
        this.deletable = false;
    }

    public Privilege(Boolean addable, Boolean viewable, Boolean updatable, Boolean deletable) {
        this.addable = addable;
        this.viewable = viewable;
        this.updatable = updatable;
        this.deletable = deletable;
    }
}


