package com.coreteam.okr.dto.invite;

import com.coreteam.okr.constant.InviteBelongEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: InviteMemberDTO
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/27 15:37
 * @Version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InviteMemberDTO {
    @NotNull
    private Long entityId;

    @NotNull
    private InviteBelongEnum type;

    private List<String> emails;

    private List<Long> userIds;
}
