package com.coreteam.okr.wx.controller;

import com.coreteam.okr.dto.objective.*;
import com.coreteam.okr.service.KeyResultService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: KeyResultController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/08/06 11:35
 * @Version 1.0.0
 */
@RestController
@RequestMapping("key_results")
@AuditLogAnnotation(value = "wenxin key_result接口")
@Slf4j
public class KeyResultController {
    @PostMapping()
    @ApiOperation("添加key-result到指定的objective")
    public Long insertObjectiveKeyResult(@RequestBody @Valid InsertObjectiveKeyResultDTO insertObjectiveKeyResultDTO) {
        return keyResultService.insertObjectiveKeyResult(insertObjectiveKeyResultDTO);
    }

    @PutMapping("/info")
    @ApiOperation("更新key-result的基本信息")
    public void updateObjectiveKeyResultInfo(@RequestBody @Valid UpdateObjectiveKeyResulInfoDTO updateObjectiveKeyResultDTO) {
        keyResultService.updateObjectiveKeyResultInfo(updateObjectiveKeyResultDTO);
    }

    @PutMapping("/status")
    @ApiOperation("更新key-result的weight，value等状态信息")
    public void updateObjectiveKeyResultStatus(@RequestBody @Valid UpdateObjectiveKeyResulStatusDTO updateObjectiveKeyResultDTO){
        keyResultService.updateObjectiveKeyResultStatus(updateObjectiveKeyResultDTO);
    }

    @PutMapping("/progress")
    @ApiOperation("更新key-result的progress")
    public void updateObjectiveKeyResultProgress(@RequestBody @Valid UpdateObjectiveKeyResulProgressDTO updateObjectiveKeyResulProgressDTO){
        keyResultService.updateObjectiveKeyResultProgress(updateObjectiveKeyResulProgressDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除key-result信息")
    public void deleteObjectiveKeyResult(@PathVariable("id") @NotNull Long keyResultId) {
        keyResultService.deleteObjectiveKeyResult(keyResultId);
    }

    @GetMapping("{id}")
    @ApiOperation("根据id获取key-result详细信息")
    public ObjectiveKeyResultDTO getObjectiveKeyResult(@PathVariable("id") @NotNull Long keyResultId){
        return keyResultService.getKeyResult(keyResultId);

    }

    @PostMapping("/comment")
    @ApiOperation("给key-result添加comment")
    public void insertObjectiveKeyResultComment(@RequestBody @Valid InsertObjectiveKeyResultCommentDTO insertObjectiveKeyResultCommentDTO) {
        keyResultService.insertObjectiveKeyResultComment(insertObjectiveKeyResultCommentDTO);
    }

    @PostMapping("/comment/replay")
    @ApiOperation("给key-result的comment添加replay")
    public void insertObjectiveKeyResultCommentReplay(@RequestBody @Valid InsertObjectiveKeyResultCommentReplyDTO insertObjectiveKeyResultCommentReplyDTO) {
        keyResultService.insertObjectiveKeyResultCommentReplay(insertObjectiveKeyResultCommentReplyDTO);
    }

    @GetMapping("/{id}/comments")
    @ApiOperation("分页获取key-result的comment信息")
    public PageInfo<ObjectiveKeyResultCommentDTO> listObjectiveResultComments(@PathVariable("id") @NotNull Long keyResultId, Integer pageNumber, Integer pageSize) {
        ListObjectiveKeyResultCommentPageReqeustDTO listObjectiveKeyResultCommentPageReqeust=new ListObjectiveKeyResultCommentPageReqeustDTO();
        listObjectiveKeyResultCommentPageReqeust.setKeyResultId(keyResultId);
        listObjectiveKeyResultCommentPageReqeust.setPageNumber(pageNumber);
        listObjectiveKeyResultCommentPageReqeust.setPageSize(pageSize);
        return keyResultService.listObjectiveKeyResultComments(listObjectiveKeyResultCommentPageReqeust);
    }

    @Autowired
    private KeyResultService keyResultService;

}
