package com.coreteam.okr.service;

import com.coreteam.okr.dto.objective.*;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName: KeyResultService
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/12 8:23
 * @Version 1.0.0
 */
public interface KeyResultService {

    /**
     *  insert key result
     * @param dto
     * @return
     */
    Long insertObjectiveKeyResult(InsertObjectiveKeyResultDTO dto);


    /**
     *  update key result base info
     * @param dto
     */
    void updateObjectiveKeyResultInfo(UpdateObjectiveKeyResulInfoDTO dto);

    /**
     * update key result info and change key result progress
     * @param dto
     */
    void updateObjectiveKeyResultStatus(UpdateObjectiveKeyResulStatusDTO dto);


    /**
     *
     * @param updateObjectiveKeyResulReviewDTO
     */
    void updateObjectiveKeyResultReViewInfo(UpdateObjectiveKeyResulReviewDTO updateObjectiveKeyResulReviewDTO);


    /**
     * update objective key result progress
     * @param updateObjectiveKeyResulProgressDTO
     */
    void updateObjectiveKeyResultProgress(UpdateObjectiveKeyResulProgressDTO updateObjectiveKeyResulProgressDTO);

    /**
     * delete key result and comment,change objective progress
     * @param objectiveId
     */
    void deleteObjectiveKeyResult(Long objectiveId);

    /**
     * get key result
     * @param keyResultId
     * @return
     */
    ObjectiveKeyResultDTO getKeyResult(Long keyResultId);

    /**
     *  add key result comment
     * @param commentDTO
     * @return
     */
    Long insertObjectiveKeyResultComment(InsertObjectiveKeyResultCommentDTO commentDTO);


    /**
     *  add key result comment replay
     * @param insertReplayDTO
     * @return
     */
    Long insertObjectiveKeyResultCommentReplay(InsertObjectiveKeyResultCommentReplyDTO insertReplayDTO);




    /**
     * page list key result comments
     * @param request
     * @return
     */
    PageInfo<ObjectiveKeyResultCommentDTO> listObjectiveKeyResultComments(ListObjectiveKeyResultCommentPageReqeustDTO request);


    /**
     * 记录key-result中上周的progress信息
     */
    void recordKeyResultLastProgress();

    void combinePrivilege(ObjectiveKeyResultDTO keyResult);

}
