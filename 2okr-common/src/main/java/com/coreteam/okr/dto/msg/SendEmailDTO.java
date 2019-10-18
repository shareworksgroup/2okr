package com.coreteam.okr.dto.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName: SendEmailDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/29 11:04
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendEmailDTO {
    private String to;
    private String title;
    private String content;
    private Map<String,byte[]> attachments;
}
