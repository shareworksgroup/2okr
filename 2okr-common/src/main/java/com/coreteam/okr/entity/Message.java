package com.coreteam.okr.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * message
 * @author 
 */
@Data
public class Message extends BaseEntity implements Serializable {
    /**
     * primary key
     */
    private Long id;

    private String title;

    private String message;

    private static final long serialVersionUID = 1L;

    public Message(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public Message() {
    }
}