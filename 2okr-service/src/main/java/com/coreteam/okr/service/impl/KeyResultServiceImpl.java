package com.coreteam.okr.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.coreteam.core.exception.CustomerException;
import com.coreteam.core.untils.BeanConvertUtils;
import com.coreteam.okr.Authorization.KeyResultPermissionRulesParser;
import com.coreteam.okr.Authorization.ObjectivePermissionRulesParser;
import com.coreteam.okr.constant.*;
import com.coreteam.okr.dao.KeyResultCommentDAO;
import com.coreteam.okr.dao.KeyResultCommentReplyDAO;
import com.coreteam.okr.dao.KeyResultDAO;
import com.coreteam.okr.dao.ObjectiveDAO;
import com.coreteam.okr.dto.Authorization.ObjectiveResourcesDesc;
import com.coreteam.okr.dto.Authorization.Privilege;
import com.coreteam.okr.dto.Notify.KeyResultHasNewCommentSystemNotifyDTO;
import com.coreteam.okr.dto.Notify.KeyResultUpdateSystemNotifyDTO;
import com.coreteam.okr.dto.member.MemberDTO;
import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.dto.user.UserInfoDTO;
import com.coreteam.okr.entity.KeyResult;
import com.coreteam.okr.entity.KeyResultComment;
import com.coreteam.okr.entity.KeyResultCommentReply;
import com.coreteam.okr.entity.Objective;
import com.coreteam.okr.manager.Notify;
import com.coreteam.okr.manager.NotifyManager;
import com.coreteam.okr.service.KeyResultService;
import com.coreteam.okr.service.MemberService;
import com.coreteam.okr.service.OkrService;
import com.coreteam.okr.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName: KeyResultServiceImpl
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/12 8:16
 * @Version 1.0.0
 */
@Service
public class KeyResultServiceImpl extends BaseService implements KeyResultService {

    @Autowired
    private ObjectivePermissionRulesParser objectivePermissionRulesParser;
    @Autowired
    private KeyResultPermissionRulesParser keyResultPermissionRulesParser;

    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    @Transactional
    public Long insertObjectiveKeyResult(InsertObjectiveKeyResultDTO dto) {
        ObjectiveDTO objectiveDTO = checkObjectiveStatus(dto.getObjectiveId());
        if (!okrService.hasPermission(dto.getObjectiveId(), PrivilegeTypeEnum.KEY_RESULT_ADDABLE)) {
            throw new CustomerException(" has no permission to operate the objective");
        }
        KeyResult keyResult = new KeyResult();
        BeanConvertUtils.copyEntityProperties(dto, keyResult);
        keyResult.setValue(dto.getMetricStartValue());
        keyResult.setProgress(0.0);
        keyResult.setLastProgress(0.0);
        UserInfoDTO owner = userService.getUserInfoById(dto.getOwnerId());
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        if (owner != null && !StringUtils.isEmpty(owner.getName())) {
            keyResult.setOwnerName(owner.getName());
            keyResult.setUpdateUserId(dto.getOwnerId());
            keyResult.setUpdateUserName(owner.getName());
        }
        keyResult.setCreatedName(currentUser.getName());
        keyResult.setCreatedUser(currentUser.getId());
        keyResult.initializeForInsert();
        this.keyResultDAO.insertSelective(keyResult);
        this.okrService.updateObjectiveProgress(objectiveDTO);
        return keyResult.getId();
    }

    @Transactional
    @Override
    public void updateObjectiveKeyResultInfo(UpdateObjectiveKeyResulInfoDTO updateObjectiveKeyResultDTO) {
        checkObjectiveStatus(updateObjectiveKeyResultDTO.getObjectiveId());
        if (!hasPermission(updateObjectiveKeyResultDTO.getId(), PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException(" has no permission to update the key result");
        }
        if (updateObjectiveKeyResultDTO.getOwnerId() != null) {
            UserInfoDTO owner = userService.getUserInfoById(updateObjectiveKeyResultDTO.getOwnerId());
            if (owner != null) {
                updateObjectiveKeyResultDTO.setOwnerName(owner.getName());
            }
        }
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        KeyResult keyResult = new KeyResult();
        BeanConvertUtils.copyEntityProperties(updateObjectiveKeyResultDTO, keyResult);
        keyResult.setUpdateUserName(currentUser.getName());
        keyResult.setUpdateUserId(currentUser.getId());
        keyResult.initializeForUpdate();
        this.keyResultDAO.updateByPrimaryKeySelective(keyResult);
    }

    @Override
    @Transactional
    public void updateObjectiveKeyResultStatus(UpdateObjectiveKeyResulStatusDTO updateObjectiveKeyResultDTO) {
        ObjectiveDTO objectiveDTO = checkObjectiveStatus(updateObjectiveKeyResultDTO.getObjectiveId());
        if (!hasPermission(updateObjectiveKeyResultDTO.getId(), PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException(" has no permission to update the key result");
        }
        KeyResult oldKeyResult = this.keyResultDAO.selectByPrimaryKey(updateObjectiveKeyResultDTO.getId());
        if (!df.format(updateObjectiveKeyResultDTO.getMetricStartValue()).equals(df.format(oldKeyResult.getMetricStartValue()))
                || !df.format(updateObjectiveKeyResultDTO.getMetricEndValue()).equals(df.format(oldKeyResult.getMetricEndValue()))
                || !df.format(updateObjectiveKeyResultDTO.getWeight()).equals(df.format(oldKeyResult.getWeight()))) {
            //start  end weight 发生变化
            Double value = Double.valueOf(df.format(updateObjectiveKeyResultDTO.getMetricStartValue() + ((updateObjectiveKeyResultDTO.getMetricEndValue() - updateObjectiveKeyResultDTO.getMetricStartValue()) * oldKeyResult.getProgress())));
            updateObjectiveKeyResultDTO.setValue(value);
        } else {
            //改变value的值
            updateObjectiveKeyResultDTO.setMetricEndValue(oldKeyResult.getMetricEndValue());
            updateObjectiveKeyResultDTO.setMetricStartValue(oldKeyResult.getMetricStartValue());
            updateObjectiveKeyResultDTO.setWeight(oldKeyResult.getWeight());
            if (updateObjectiveKeyResultDTO.getValue() < Math.min(updateObjectiveKeyResultDTO.getMetricEndValue(), updateObjectiveKeyResultDTO.getMetricStartValue())
                    || updateObjectiveKeyResultDTO.getValue() > Math.max(updateObjectiveKeyResultDTO.getMetricEndValue(), updateObjectiveKeyResultDTO.getMetricStartValue())) {
                throw new CustomerException(" the key result value should between start value and end value");
            }
        }
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        KeyResult keyResult = new KeyResult();
        BeanConvertUtils.copyEntityProperties(updateObjectiveKeyResultDTO, keyResult);
        calObjectiveKeyResultProgress(keyResult);
        keyResult.setUpdateUserName(currentUser.getName());
        keyResult.setUpdateUserId(currentUser.getId());
        keyResult.initializeForUpdate();
        this.keyResultDAO.updateByPrimaryKeySelective(keyResult);
        this.okrService.updateObjectiveProgress(objectiveDTO);
    }

    @Override
    public void updateObjectiveKeyResultReViewInfo(UpdateObjectiveKeyResulReviewDTO updateObjectiveKeyResulReviewDTO) {
        checkObjectiveStatus(updateObjectiveKeyResulReviewDTO.getObjectiveId());
        if (!hasPermission(updateObjectiveKeyResulReviewDTO.getId(), PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException(" has no permission to update the key result");
        }
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        KeyResult keyResult = new KeyResult();
        BeanConvertUtils.copyEntityProperties(updateObjectiveKeyResulReviewDTO, keyResult);
        keyResult.setUpdateUserName(currentUser.getName());
        keyResult.setUpdateUserId(currentUser.getId());
        keyResult.initializeForUpdate();
        this.keyResultDAO.updateByPrimaryKeySelective(keyResult);
    }

    @Override
    @Transactional
    public void updateObjectiveKeyResultProgress(UpdateObjectiveKeyResulProgressDTO updateObjectiveKeyResulProgressDTO) {
        UserInfoDTO currentUser = userService.getCurrentUserInfo();
        if (!hasPermission(updateObjectiveKeyResulProgressDTO.getId(), PrivilegeTypeEnum.UPDATABLE)) {
            throw new CustomerException(" has no permission to update the key result");
        }
        KeyResult keyResult = this.keyResultDAO.selectByPrimaryKey(updateObjectiveKeyResulProgressDTO.getId());
        ObjectiveDTO objectiveDTO = checkObjectiveStatus(keyResult.getObjectiveId());
        updateObjectiveKeyResulProgressDTO.setProgress(Double.valueOf(df.format(updateObjectiveKeyResulProgressDTO.getProgress())));
        Double value = Double.valueOf(df.format(keyResult.getMetricStartValue() + ((keyResult.getMetricEndValue() - keyResult.getMetricStartValue()) * updateObjectiveKeyResulProgressDTO.getProgress())));
        keyResult.setProgress(updateObjectiveKeyResulProgressDTO.getProgress());
        keyResult.setValue(value);
        keyResult.setUpdateUserId(currentUser.getId());
        keyResult.setUpdateUserName(currentUser.getName());
        Date lastUpdate = keyResult.getUpdatedAt();
        keyResult.initializeForUpdate();
        this.keyResultDAO.updateByPrimaryKeySelective(keyResult);
        this.okrService.updateObjectiveProgress(objectiveDTO);
        //发送k-r更新的通知
        if (lastUpdate != null && (System.currentTimeMillis() - lastUpdate.getTime() > 1000 * 60 * 30)) {
            sendKeyResultUpdateNotification(keyResult, objectiveDTO);
        }

    }

    private ObjectiveDTO checkObjectiveStatus(Long ObjectiveId) {
        ObjectiveDTO objectiveDTO = this.okrService.getObjectiveById(ObjectiveId);
        if (objectiveDTO == null) {
            throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_ID_NOT_EXIST);
        }
        if (objectiveDTO.getStatus() == ObjectiveStatusEnum.CLOSED) {
            throw new CustomerException("the Objective is closed");
        }
        return objectiveDTO;
    }


    @Transactional
    @Override
    public void deleteObjectiveKeyResult(Long keyResultId) {
        KeyResult keyResult = this.keyResultDAO.selectByPrimaryKey(keyResultId);
        if (keyResult == null) {
            return;
        }
        ObjectiveDTO objectiveDTO = this.okrService.getObjectiveById(keyResult.getObjectiveId());
        if (objectiveDTO == null) {
            throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_ID_NOT_EXIST);
        }
        if (!hasPermission(keyResultId, PrivilegeTypeEnum.DELETABLE)) {
            throw new CustomerException(" has no permission to update the key result");
        }
        this.keyResultDAO.deleteByPrimaryKey(keyResultId);
        this.deleteKeyResultComments(keyResultId);
        this.okrService.updateObjectiveProgress(objectiveDTO);
    }

    @Override
    public ObjectiveKeyResultDTO getKeyResult(Long keyResultId) {
        ObjectiveKeyResultDTO resultDTO = new ObjectiveKeyResultDTO();
        KeyResult result = this.keyResultDAO.selectByPrimaryKey(keyResultId);
        if (result == null) {
            return new ObjectiveKeyResultDTO();
        }
        BeanConvertUtils.copyEntityProperties(result, resultDTO);
        combinePrivilege(resultDTO);
        return resultDTO;
    }

    private void calObjectiveKeyResultProgress(KeyResult keyResult) {
        Double progress = Double.valueOf(df.format(Double.valueOf(keyResult.getValue() - keyResult.getMetricStartValue()) / Double.valueOf(keyResult.getMetricEndValue() - keyResult.getMetricStartValue())));
        if (progress < 0) {
            new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_KEY_RESULT_VALUE_ILLEGAL);
        } else if (progress > 1) {
            progress = 1.00;
        }
        keyResult.setProgress(progress);
    }


    @Override
    public Long insertObjectiveKeyResultComment(InsertObjectiveKeyResultCommentDTO commentDTO) {
        KeyResult keyResult = this.keyResultDAO.selectByPrimaryKey(commentDTO.getKeyResultId());
        if (keyResult == null) {
            throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_KEY_RESULT_ID_NOT_EXIST);
        }

        KeyResultComment comment = new KeyResultComment();
        comment.setComment(commentDTO.getComment());
        comment.setKeyResultId(commentDTO.getKeyResultId());
        UserInfoDTO currentUser = getCurrentUser();
        comment.setCommentUserId(currentUser.getId());
        comment.setCommentUserName(currentUser.getName());
        comment.initializeForInsert();
        this.keyResultCommentDAO.insert(comment);
        ObjectiveDTO objectiveDTO = this.okrService.getObjectiveById(keyResult.getObjectiveId());
        //key-Result增加评论时，添加提醒
        List<UserInfoDTO> notifies=new ArrayList<>();
        Set<Long> notifiesId=new HashSet<>();
        notifiesId.add(keyResult.getOwnerId());
        if(commentDTO.getMentions()!=null){
            notifiesId.addAll(commentDTO.getMentions());
        }
        //移除自己
        notifiesId.remove(currentUser.getId());
        if(!CollectionUtils.isEmpty(notifiesId)){
            notifiesId.forEach(userId->{
                try{
                    UserInfoDTO userInfo = userService.getUserInfoById(userId);
                    notifies.add(userInfo);
                }catch (Exception e){

                }
            });
            sendKeyResultNewComentNotifycation(keyResult, objectiveDTO, currentUser.getName(),notifies);
        }
        return comment.getId();
    }

    @Override
    public Long insertObjectiveKeyResultCommentReplay(InsertObjectiveKeyResultCommentReplyDTO insertReplayDTO) {
        KeyResultComment keyResultComment = this.keyResultCommentDAO.selectByPrimaryKey(insertReplayDTO.getCommentId());
        if (keyResultComment == null) {
            throw new CustomerException("can not find objective key result comment for special id");
        }
        KeyResult keyResult = this.keyResultDAO.selectByPrimaryKey(keyResultComment.getKeyResultId());
        if (keyResult == null) {
            throw new CustomerException(ValidateErrorMessageEnum.OBJECTIVE_KEY_RESULT_ID_NOT_EXIST);
        }
        UserInfoDTO currentUser = getCurrentUser();
        KeyResultCommentReply reply=new KeyResultCommentReply();
        reply.setCommentId(insertReplayDTO.getCommentId());
        reply.setContext(insertReplayDTO.getReplay());
        reply.setReplayUserId(currentUser.getId());
        reply.setReplayUserName(currentUser.getName());
        reply.initializeForInsert();
        this.commentReplyDAO.insertSelective(reply);
        //key-Result增加评论时，添加提醒
        ObjectiveDTO objectiveDTO = this.okrService.getObjectiveById(keyResult.getObjectiveId());
        //key-Result增加评论时，添加提醒
        List<UserInfoDTO> notifies=new ArrayList<>();
        Set<Long> notifiesId=new HashSet<>();
        notifiesId.add(keyResult.getOwnerId());
        notifiesId.add(keyResultComment.getCommentUserId());
        if(insertReplayDTO.getMentions()!=null){
            notifiesId.addAll(insertReplayDTO.getMentions());
        }
        //移除自己
        notifiesId.remove(currentUser.getId());
        if(!CollectionUtils.isEmpty(notifiesId)){
            notifiesId.forEach(userId->{
                try{
                    UserInfoDTO userInfo = userService.getUserInfoById(userId);
                    notifies.add(userInfo);
                }catch (Exception e){

                }
            });
            sendKeyResultNewComentNotifycation(keyResult, objectiveDTO, currentUser.getName(),notifies);
        }
        return reply.getId();
    }


    @Override
    public PageInfo<ObjectiveKeyResultCommentDTO> listObjectiveKeyResultComments(ListObjectiveKeyResultCommentPageReqeustDTO request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<KeyResultComment> list = this.keyResultCommentDAO.listKeyResultCommentsByKeyResultId(request.getKeyResultId());
        PageInfo pageInfo = new PageInfo(list);
        List<ObjectiveKeyResultCommentDTO> dtoList = BeanConvertUtils.batchTransform(ObjectiveKeyResultCommentDTO.class, list);
        combineKeyResultReply(dtoList);
        pageInfo.setList(dtoList);
        return pageInfo;
    }

    public void combineKeyResultReply(List<ObjectiveKeyResultCommentDTO> comments) {
        if (!CollectionUtils.isEmpty(comments)) {
            comments.forEach(comment -> {
                List<KeyResultCommentReply> replies = this.commentReplyDAO.listRepaysByCommentId(comment.getId());
                if (!CollectionUtils.isEmpty(replies)) {
                    comment.setReplyList(BeanConvertUtils.batchTransform(KeyResultCommentReplyDTO.class, replies));
                }

            });
        }
    }

    @Override
    public void recordKeyResultLastProgress() {
        List<KeyResult> updateKeyResultList = this.keyResultDAO.listKeyResultForOpenObjective();
        if (!CollectionUtils.isEmpty(updateKeyResultList)) {
            for (KeyResult kr : updateKeyResultList) {
                kr.setLastProgress(kr.getProgress());
                this.keyResultDAO.updateByPrimaryKeySelective(kr);
            }
        }
    }

    private void deleteKeyResultComments(Long keyResultId) {
        this.keyResultCommentDAO.deleteByKeyResultId(keyResultId);
    }

    private Boolean hasPermission(Long id, PrivilegeTypeEnum type) {
        KeyResult kr = this.keyResultDAO.selectByPrimaryKey(id);
        if (kr == null) {
            return false;
        }
        ObjectiveKeyResultDTO krdto = new ObjectiveKeyResultDTO();
        BeanUtils.copyProperties(kr, krdto);
        Privilege privilege = getKeyResultPrivilege(krdto);
        switch (type) {
            case UPDATABLE:
                return privilege.getUpdatable();
            case DELETABLE:
                return privilege.getDeletable();
        }
        return false;
    }

    @Override
    public void combinePrivilege(ObjectiveKeyResultDTO keyResult) {
        if (keyResult == null) {
            return;
        }
        keyResult.setPrivilege(getKeyResultPrivilege(keyResult));
    }

    public Privilege getKeyResultPrivilege(ObjectiveKeyResultDTO keyResult) {
        Objective objective = this.objectiveDAO.selectByPrimaryKey(keyResult.getObjectiveId());

        ObjectiveDTO objectiveDTO = new ObjectiveDTO();
        BeanUtils.copyProperties(objective, objectiveDTO);
        List<MemberDTO> orgOwner = null;
        List<MemberDTO> teamOwner = null;
        switch (objectiveDTO.getLevel()) {
            case ORGANIZATION:
                orgOwner = this.memberService.listOrganizationMembers(objectiveDTO.getOrganizationId(), true);
                break;
            case TEAM:
                orgOwner = this.memberService.listOrganizationMembers(objectiveDTO.getOrganizationId(), true);
                teamOwner = this.memberService.listTeamMembers(objectiveDTO.getTeamId(), true);
                break;
            case MEMBER:
                break;
        }
        ObjectiveResourcesDesc desc = new ObjectiveResourcesDesc();
        desc.setObjective(objectiveDTO);
        desc.setOrgOwner(orgOwner);
        desc.setTeamOwner(teamOwner);

        UserInfoDTO currentUser = getCurrentUser();
        Boolean hasObjectivePermission = objectivePermissionRulesParser.hasPermission(currentUser, desc);
        if (hasObjectivePermission) {
            return keyResultPermissionRulesParser.getFullPrivilege();
        } else {
            return keyResultPermissionRulesParser.getUserPrivilege(currentUser, keyResult);
        }
    }

    private void sendKeyResultUpdateNotification(KeyResult keyResult, ObjectiveDTO objective) {
        notifyManager.sendNotify(new Notify<KeyResultUpdateSystemNotifyDTO>() {
            @Override
            public NotifyTypeEnum type() {
                return NotifyTypeEnum.SYSTEM;
            }

            @Override
            public KeyResultUpdateSystemNotifyDTO message() {
                return new KeyResultUpdateSystemNotifyDTO(keyResult.getName(), objective.getName(), keyResult.getOwnerName(), keyResult.getOwnerId(), objective.getId());
            }

            @Override
            public NotifyBusinessType businessType() {
                return NotifyBusinessType.SYSTEM_KEY_RESULT_UPDATE;
            }
        });

    }

    private void sendKeyResultNewComentNotifycation(KeyResult keyResult, ObjectiveDTO objective, String commenter,List<UserInfoDTO> notifiers) {
        if(!CollectionUtils.isEmpty(notifiers)){
            notifiers.forEach(notify->{
                notifyManager.sendNotify(new Notify<KeyResultHasNewCommentSystemNotifyDTO>() {
                    @Override
                    public NotifyTypeEnum type() {
                        return NotifyTypeEnum.SYSTEM;
                    }

                    @Override
                    public KeyResultHasNewCommentSystemNotifyDTO message() {
                        return new KeyResultHasNewCommentSystemNotifyDTO(keyResult.getName(), commenter, notify.getName(), notify.getId(), objective.getId());
                    }

                    @Override
                    public NotifyBusinessType businessType() {
                        return NotifyBusinessType.SYSTEM_KEY_RESULT_HAS_NEW_COMMENT;
                    }
                });
            });
        }
    }

    @Autowired
    private KeyResultDAO keyResultDAO;
    @Autowired
    private KeyResultCommentDAO keyResultCommentDAO;
    @Autowired
    private OkrService okrService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotifyManager notifyManager;
    @Autowired
    private ObjectiveDAO objectiveDAO;
    @Autowired
    private MemberService memberService;
    @Autowired
    private KeyResultCommentReplyDAO commentReplyDAO;
}
