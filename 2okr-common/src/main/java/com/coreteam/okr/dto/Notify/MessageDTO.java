package com.coreteam.okr.dto.Notify;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @ClassName: MessageDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/11 16:12
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO {
    private Long id;

    private String title;

    private JSONObject message;

}
