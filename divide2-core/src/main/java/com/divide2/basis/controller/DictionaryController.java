package com.divide2.basis.controller;

import com.divide2.core.er.Responser;
import com.divide2.basis.model.Dictionary;
import com.divide2.basis.service.DictionaryService;
import com.divide2.basis.vo.DictionaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bvvy
 * @date 2018/8/10
 */
@RestController
@RequestMapping("/v1/dictionary")
@Api(tags = "字典")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/all")
    @ApiOperation("查询所有数据，前端处理 做filter")
    public ResponseEntity<List<DictionaryVO>> all() {
        List<Dictionary> dictionaries = dictionaryService.findAll();
        List<DictionaryVO> vos = dictionaries.stream().map(DictionaryVO::of).collect(Collectors.toList());
        return Responser.ok(vos);
    }

    @GetMapping("/{groupCode}")
    @ApiOperation("查询一组字典")
    public ResponseEntity<List<Dictionary>> listByGroup(@PathVariable String groupCode) {
        return Responser.ok(dictionaryService.findByGroup(groupCode));
    }
}
