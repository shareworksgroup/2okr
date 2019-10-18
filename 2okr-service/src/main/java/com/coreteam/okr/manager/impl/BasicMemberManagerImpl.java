package com.coreteam.okr.manager.impl;

import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.constant.MemberRoleEnum;
import com.coreteam.okr.constant.MemberStatusEnum;
import com.coreteam.okr.constant.MemberTypeEnum;
import com.coreteam.okr.dao.MemberDAO;
import com.coreteam.okr.dao.SubscribeDAO;
import com.coreteam.okr.dto.member.ChangeMemberRoleDTO;
import com.coreteam.okr.dto.member.InsertMemberDTO;
import com.coreteam.okr.dto.subscribe.SubscriptionDTO;
import com.coreteam.okr.entity.Member;
import com.coreteam.okr.manager.BasicMemberManager;
import com.coreteam.okr.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @ClassName: BasicMemberManagerImpl
 * @Description TODO
 * @Author jianyong.jiang
 * @Date 2019/3/28 16:58
 * @Version 1.0.0
 */
@Service
public class BasicMemberManagerImpl implements BasicMemberManager {

    @Override
    public long insertMember(InsertMemberDTO insertMemberDTO,MemberStatusEnum status) {
        return this.insertMemberImpl(insertMemberDTO, MemberRoleEnum.MEMBER,status);
    }

    /**
     * this method should only invoked by internal
     */
    @Override
    public long insertMemberAsOwner(InsertMemberDTO insertMemberDTO,MemberStatusEnum status) {
        return this.insertMemberImpl(insertMemberDTO, MemberRoleEnum.OWNER,status);
    }

    @Override
    public void changeMemberRole(ChangeMemberRoleDTO dto) {
        Member member=null;
        if(MemberTypeEnum.TEAM==dto.getType()){
            member= this.memberDAO.findTeamMember(dto.getEntityId(), dto.getUserId());
        }else{
            member=this.memberDAO.findOrganizationMember(dto.getEntityId(), dto.getUserId());
        }
        member.setRole(dto.getRole());
        member.setUserName(dto.getUserName());
        member.initializeForUpdate();
        this.memberDAO.updateByPrimaryKeySelective(member);
    }

    @Override
    public Member checkMemberExist(Long entityId, Long userId, MemberTypeEnum type) {
        Member member=null;
        if(type== MemberTypeEnum.TEAM){
            member = this.memberDAO.findTeamMember(entityId, userId);
        }else if (type==MemberTypeEnum.ORGANIZATION){
            member = this.memberDAO.findOrganizationMember(entityId, userId);
        }
        return member;
    }

    @Override
    public Member checkMemberExistIgnoreStatus(Long entityId, Long userId, String email, MemberTypeEnum type) {
        Member member=null;
        if(userId!=null){
            if(type== MemberTypeEnum.TEAM){
                member = this.memberDAO.findTeamMemberIgnoreStatus(entityId, userId);
            }else if (type==MemberTypeEnum.ORGANIZATION){
                member = this.memberDAO.findOrganizationMemberIgnoreStatus(entityId, userId);
            }
        }else{
            if(type== MemberTypeEnum.TEAM){
                member = this.memberDAO.findTeamMemberByEmailBeforeRegister(entityId, email);
            }else if (type==MemberTypeEnum.ORGANIZATION){
                member = this.memberDAO.findOrganizationMemberByEmailBeforeRegister(entityId, email);
            }
        }
        return member;
    }


    private long insertMemberImpl(InsertMemberDTO insertMemberDTO, MemberRoleEnum roleEnum,MemberStatusEnum status) {
        Objects.requireNonNull(insertMemberDTO);
        if(check){
            SubscriptionDTO subscription = subscribeService.getActiveSubscription(insertMemberDTO.getOrganizationId());
            if(subscription==null||subscription.getId()==null){
                throw new CustomerException(" the organization has not subscription,please create subscription in setting");
            }else{
                Integer size = this.memberDAO.countOrganizationMember(insertMemberDTO.getOrganizationId());
                if(subscription.getMaxUserAmount()<=size){
                    throw new CustomerException("The size of organizations has reached the upper limit "+size+", please subscribe to a larger one");
                }
            }
        }

         Member member=null;
        if(insertMemberDTO.getType()== MemberTypeEnum.TEAM){
            member=checkMemberExist(insertMemberDTO.getTeamId(),insertMemberDTO.getUserId(),MemberTypeEnum.TEAM);
        }else if (insertMemberDTO.getType()==MemberTypeEnum.ORGANIZATION){
            member=checkMemberExist(insertMemberDTO.getOrganizationId(),insertMemberDTO.getUserId(),MemberTypeEnum.ORGANIZATION);
        }else{
            throw new CustomerException("member type should be team or organization");
        }
        if(member!=null){
            return member.getId();
        }
        member = new Member();
        member.setRole(roleEnum);
        member.setType(insertMemberDTO.getType());
        BeanConvertUtils.copyProperties(insertMemberDTO, member);
        member.setStatus(status);
        member.initializeForInsert();
        this.memberDAO.insert(member);
        if(check){
            subscribeService.updateReminderUserAmount(insertMemberDTO.getOrganizationId());
        }

        return member.getId();
    }

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private SubscribeService subscribeService;

    @Value("${organization.subscription.check}")
    private Boolean check=false;
}
