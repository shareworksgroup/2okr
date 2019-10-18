package com.coreteam.okr.Authorization;

import com.coreteam.okr.dto.Authorization.Privilege;
import com.coreteam.okr.dto.user.UserInfoDTO;


/**
 * @ClassName: PermissionRulesParser
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/29 9:55
 * @Version 1.0.0
 */
public interface PermissionRulesParser {

    Privilege getUserPrivilege(UserInfoDTO user, Object resources);

}
