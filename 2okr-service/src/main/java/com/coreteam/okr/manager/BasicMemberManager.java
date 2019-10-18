package com.coreteam.okr.manager;

import com.coreteam.okr.constant.MemberStatusEnum;
import com.coreteam.okr.constant.MemberTypeEnum;
import com.coreteam.okr.dto.member.ChangeMemberRoleDTO;
import com.coreteam.okr.dto.member.InsertMemberDTO;
import com.coreteam.okr.entity.Member;

/**
 * @ClassName: BasicMemberManager
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/28 16:56
 * @Version 1.0.0
 */
public interface BasicMemberManager {
    long insertMember(InsertMemberDTO insertMemberDTO, MemberStatusEnum status);

    long insertMemberAsOwner(InsertMemberDTO insertMemberDTO, MemberStatusEnum status);

    void changeMemberRole(ChangeMemberRoleDTO dto);

    Member checkMemberExist(Long entityId, Long userId, MemberTypeEnum type);

    Member checkMemberExistIgnoreStatus(Long entityId, Long userId, String email, MemberTypeEnum type);

}
