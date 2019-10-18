package com.coreteam.okr.dto.Authorization;

import com.coreteam.okr.constant.PrivilegeTypeEnum;


/**
 * @ClassName: PrivilegeFactory
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/29 10:31
 * @Version 1.0.0
 */
public class PrivilegeFactory {
    public static Privilege buildSelectivePrivilege(PrivilegeTypeEnum... type) {
        Privilege privilege = new Privilege();
        if (type != null) {
            for (PrivilegeTypeEnum privilegeTypeEnum : type) {
                switch (privilegeTypeEnum) {
                    case ADDABLE:
                        privilege.setAddable(true);
                        break;
                    case DELETABLE:
                        privilege.setDeletable(true);
                        break;
                    case VIEWABLE:
                        privilege.setViewable(true);
                        break;
                    case UPDATABLE:
                        privilege.setUpdatable(true);
                        break;
                }
            }
        }
        return privilege;
    }

    public static Privilege buildViewPrivilage() {
        Privilege privilege = new Privilege();
        privilege.setViewable(true);
        return privilege;
    }

    public static Privilege buildFullProvolage() {
        Privilege privilege = new Privilege(true, true, true, true);
        return privilege;
    }

    public static OrganizationPrivilege buildViewOrganizationPrivilage() {
        OrganizationPrivilege privilege = new OrganizationPrivilege();
        privilege.setViewable(true);
        privilege.setAddable(true);
        privilege.setTeamAddable(false);
        return privilege;
    }

    public static OrganizationPrivilege buildFullOrganizationProvolage() {
        OrganizationPrivilege privilege = new OrganizationPrivilege();
        privilege.setTeamAddable(true);
        privilege.setAddable(true);
        privilege.setDeletable(true);
        privilege.setUpdatable(true);
        privilege.setViewable(true);
        return privilege;
    }

    public static ObjectivePrivilege buildFullObjectiveProvolage() {
        ObjectivePrivilege privilege = new ObjectivePrivilege();
        privilege.setKeyResultAddable(true);
        privilege.setAddable(true);
        privilege.setDeletable(true);
        privilege.setUpdatable(true);
        privilege.setViewable(true);
        return privilege;
    }

    public static ObjectivePrivilege buildViewObjectivePrivilage() {
        ObjectivePrivilege privilege = new ObjectivePrivilege();
        privilege.setViewable(true);
        privilege.setAddable(true);
        privilege.setKeyResultAddable(false);
        return privilege;
    }

}
