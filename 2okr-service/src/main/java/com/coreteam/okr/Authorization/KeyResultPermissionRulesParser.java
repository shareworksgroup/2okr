package com.coreteam.okr.Authorization;

import com.coreteam.okr.constant.PrivilegeTypeEnum;
import com.coreteam.okr.dto.Authorization.Privilege;
import com.coreteam.okr.dto.Authorization.PrivilegeFactory;
import com.coreteam.okr.dto.objective.ObjectiveKeyResultDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import org.springframework.stereotype.Component;

/**
 * @ClassName: KeyResultPermissionRulesParser
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/30 15:20
 * @Version 1.0.0
 */
@Component
public class KeyResultPermissionRulesParser implements PermissionRulesParser {
    @Override
    public Privilege getUserPrivilege(UserInfoDTO user, Object resources) {
        if(user.getId().equals(((ObjectiveKeyResultDTO)resources).getOwnerId())){
            return PrivilegeFactory.buildSelectivePrivilege(PrivilegeTypeEnum.VIEWABLE,PrivilegeTypeEnum.UPDATABLE);
        }
        return PrivilegeFactory.buildViewPrivilage();
    }

    public Privilege getFullPrivilege(){
        return PrivilegeFactory.buildFullProvolage();
    }

}
