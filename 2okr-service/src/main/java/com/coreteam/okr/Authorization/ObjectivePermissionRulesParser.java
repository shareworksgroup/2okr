package com.coreteam.okr.Authorization;

import com.coreteam.okr.dto.Authorization.ObjectivePrivilege;
import com.coreteam.okr.dto.Authorization.ObjectiveResourcesDesc;
import com.coreteam.okr.dto.Authorization.PrivilegeFactory;
import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.objective.ObjectiveDTO;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.Objective;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: ObjectivePermissionRulesParser
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/30 9:18
 * @Version 1.0.0
 */
@Component
public class ObjectivePermissionRulesParser implements PermissionRulesParser {

    @Override
    public ObjectivePrivilege getUserPrivilege(UserInfoDTO user, Object resources) {
        ObjectiveResourcesDesc objectiveResourcesDesc = (ObjectiveResourcesDesc) resources;
        boolean hasPermission = false;
        hasPermission = hasPermission(user, objectiveResourcesDesc);
        if (hasPermission) {
            return PrivilegeFactory.buildFullObjectiveProvolage();

        } else {
            return PrivilegeFactory.buildViewObjectivePrivilage();
        }
    }

    public boolean hasPermission(UserInfoDTO user, ObjectiveResourcesDesc objectiveResourcesDesc) {
        switch (objectiveResourcesDesc.getObjective().getLevel()) {
            case ORGANIZATION:
                return  hasOrgObjectivePrivilege(user, objectiveResourcesDesc);
            case TEAM:
                return  hasTeamObjectivePrivilege(user, objectiveResourcesDesc);
            case MEMBER:
                return hasMemberObjectivePrivilege(user, objectiveResourcesDesc);

        }
        return false;
    }

    private boolean hasOrgObjectivePrivilege(UserInfoDTO user, ObjectiveResourcesDesc resources) {
        if (isObjectiveOwner(user, resources.getObjective())) {
            return true;
        } else {
            return isOrgOwner(user, resources.getOrgOwner());
        }
    }

    private boolean hasTeamObjectivePrivilege(UserInfoDTO user, ObjectiveResourcesDesc resources) {
        if (isObjectiveOwner(user, resources.getObjective())) {
            return true;
        } else if (isOrgOwner(user, resources.getOrgOwner())) {
            return true;
        } else {
            return isTeamOwner(user, resources.getTeamOwner());
        }
    }

    private boolean hasMemberObjectivePrivilege(UserInfoDTO user, ObjectiveResourcesDesc resources) {
        return isObjectiveOwner(user, resources.getObjective());
    }

    private boolean isObjectiveOwner(UserInfoDTO user, ObjectiveDTO objective) {
        return user.getId().equals(objective.getOwnerId()) || user.getId().equals(objective.getCreatedUser());
    }

    private Boolean isOrgOwner(UserInfoDTO user, List<MemberDTO> orgOwners) {
        if (CollectionUtils.isEmpty(orgOwners)) {
            return false;
        }
        for (MemberDTO memberDTO : orgOwners) {
            if (memberDTO.getUserId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    private Boolean isTeamOwner(UserInfoDTO user, List<MemberDTO> teamOwner) {
        if (CollectionUtils.isEmpty(teamOwner)) {
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
