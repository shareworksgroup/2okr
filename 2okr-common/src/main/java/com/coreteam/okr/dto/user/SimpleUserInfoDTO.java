package com.coreteam.okr.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Objects;

/**
 * @ClassName: SimpleUserInfoDTO
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/28 16:49
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleUserInfoDTO {
    private Long id;
    private String userName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleUserInfoDTO that = (SimpleUserInfoDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName);
    }
}
