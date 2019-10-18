package com.coreteam.okr.Authorization;

import com.coreteam.okr.dto.Authorization.OrganizationPrivilege;
import com.coreteam.okr.dto.Authorization.Privilege;
import com.coreteam.okr.dto.Authorization.PrivilegeFactory;
import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Objective;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: OrganizationPermissionRulesParser
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/29 10:27
 * @Version 1.0.0
 */
@Component
public class OrganizationPermissionRulesParser implements PermissionRulesParser {


    @Override
    public OrganizationPrivilege getUserPrivilege(UserInfoDTO user, Object resources) {
        List<MemberDTO> orgOwner = (List<MemberDTO>) resources;
        if(isOwner(user,orgOwner)){
            return PrivilegeFactory.buildFullOrganizationProvolage();
        }else{
            return PrivilegeFactory.buildViewOrganizationPrivilage();
        }
    }

    private boolean isOwner(UserInfoDTO user,List<MemberDTO> owners){
        if(CollectionUtils.isEmpty(owners)){
            return  false;
        }
        for (MemberDTO memberDTO : owners) {
            if (memberDTO.getUserId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

}
