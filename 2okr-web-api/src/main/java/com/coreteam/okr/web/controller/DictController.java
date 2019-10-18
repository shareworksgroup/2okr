package com.coreteam.okr.web.controller;

import com.coreteam.okr.dto.dictionary.DictDTO;
import com.coreteam.okr.dto.dictionary.InsertDictDTO;
import com.coreteam.okr.service.DictService;
import com.coreteam.web.auditlog.AuditLogAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: DictController
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/04/18 15:20
 * @Version 1.0.0
 */
@RestController
@RequestMapping("dict")
@AuditLogAnnotation(value = "dict接口")
@Slf4j
public class DictController {

    @GetMapping()
    public List<DictDTO> listDict(String dictType){
        return dictService.listDict(dictType);
    }
    @PostMapping
    public Long insertDict(@RequestBody InsertDictDTO insertDictDTO){
        return dictService.insertDict(insertDictDTO);
    }

    @PutMapping("{id}")
    public void updateDict(@PathVariable("id") @NotNull Long id, @RequestBody InsertDictDTO updateDictDTO){
        dictService.updateDict(id,updateDictDTO);
    }

    @DeleteMapping("{id}")
    public void deleteDict(@PathVariable("id") @NotNull Long id){
        dictService.deleteDict(id);
    }

    @Autowired
    private DictService dictService;
}
