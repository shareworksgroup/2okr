package com.coreteam.okr.Authorization;

import com.coreteam.okr.constant.PrivilegeTypeEnum;
import com.coreteam.okr.dto.Authorization.Privilege;
import com.coreteam.okr.dto.Authorization.PrivilegeFactory;
import com.coreteam.okr.dto.Authorization.TeamResourcesDescDTO;
import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: TeamPermissionRulesParser
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/29 10:56
 * @Version 1.0.0
 */
@Component
public class TeamPermissionRulesParser implements PermissionRulesParser {

    @Override
    public Privilege getUserPrivilege(UserInfoDTO user, Object resources) {
        TeamResourcesDescDTO teamResourcesDescDTO= (TeamResourcesDescDTO) resources;
        List<MemberDTO> orgOwners=teamResourcesDescDTO.getOrgOwner();
        List<MemberDTO> teamOwners=teamResourcesDescDTO.getTeamOwner();
        if(isOrgOwner(user,orgOwners)){
            return PrivilegeFactory.buildSelectivePrivilege(PrivilegeTypeEnum.ADDABLE, PrivilegeTypeEnum.DELETABLE, PrivilegeTypeEnum.UPDATABLE, PrivilegeTypeEnum.VIEWABLE);
        }else if (isTeamOwner(user,teamOwners)){
            return PrivilegeFactory.buildSelectivePrivilege(PrivilegeTypeEnum.UPDATABLE, PrivilegeTypeEnum.VIEWABLE);
        }else{
            return PrivilegeFactory.buildSelectivePrivilege(PrivilegeTypeEnum.VIEWABLE);
        }

    }

    private Boolean isOrgOwner(UserInfoDTO user,List<MemberDTO> orgOwners){
        if(CollectionUtils.isEmpty(orgOwners)){
            return false;
        }
        for (MemberDTO memberDTO : orgOwners) {
            if (memberDTO.getUserId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    private Boolean isTeamOwner(UserInfoDTO user,List<MemberDTO> teamOwner){
        if(CollectionUtils.isEmpty(teamOwner)){
            return false;
        }
        for (MemberDTO memberDTO : teamOwner) {
            if (memberDTO.getUserId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

}
