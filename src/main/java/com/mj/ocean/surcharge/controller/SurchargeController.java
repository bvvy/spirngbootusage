package com.mj.ocean.surcharge.controller;

import com.mj.core.data.del.SingleId;
import com.mj.core.data.resp.Messager;
import com.mj.core.er.Responser;
import com.mj.ocean.surcharge.dto.SurchargeAddDTO;
import com.mj.ocean.surcharge.dto.SurchargeQueryDTO;
import com.mj.ocean.surcharge.dto.SurchargeUpdateDTO;
import com.mj.ocean.surcharge.model.Surcharge;
import com.mj.ocean.surcharge.service.SurchargeService;
import com.mj.ocean.surcharge.vo.SurchargeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bvvy
 * @date 2018/7/18
 */

@RestController
@RequestMapping("/v1/surcharge")
@Api(tags = "附加费")
public class SurchargeController {

    private final SurchargeService surchargeService;

    public SurchargeController(SurchargeService surchargeService) {
        this.surchargeService = surchargeService;
    }

    @PostMapping
    @ApiOperation("新增附加费")
    public ResponseEntity<Messager> add(@Valid @RequestBody SurchargeAddDTO surchargeAddDTO, BindingResult br) {
        surchargeService.add(surchargeAddDTO);
        return Responser.created();
    }

    @PatchMapping
    @ApiOperation("修改附加费")
    public ResponseEntity<Messager> update(@Valid @RequestBody SurchargeUpdateDTO surchargeUpdateDTO, BindingResult br) {
        surchargeService.update(surchargeUpdateDTO);
        return Responser.updated();
    }

    @DeleteMapping
    @ApiOperation("删除附加费")
    public ResponseEntity<Messager> delete(@Valid @RequestBody SingleId del, BindingResult br) {
        surchargeService.delete(del.getId());
        return Responser.deleted();
    }

    @PatchMapping("/enable/toggle")
    @ApiOperation("切换启用状态")
    public ResponseEntity<Messager> toggleEnable(SingleId id) {
        surchargeService.toggleEnable(id.getId());
        return Responser.updated();
    }

    @GetMapping("/find")
    @ApiOperation("附加费查询")
    public ResponseEntity<Page<SurchargeVO>> find(SurchargeQueryDTO surchargeQueryDTO, Pageable pageable) {
        Page<Surcharge> surcharges = surchargeService.find(surchargeQueryDTO, pageable);
        Page<SurchargeVO> vos = surcharges.map(SurchargeVO::fromSurcharge);
        return Responser.ok(vos);
    }

    @GetMapping("/{carrierId}/{pomId}/{podId}/group")
    public ResponseEntity<List<SurchargeVO>> findSameGroup(@PathVariable Integer carrierId,
                                                           @PathVariable Integer pomId,
                                                           @PathVariable Integer podId) {
        List<Surcharge> surcharges =  surchargeService.findSameGroup(carrierId, pomId, podId);
        List<SurchargeVO> vos = surcharges.stream().map(SurchargeVO::fromSurcharge).collect(Collectors.toList());
        return Responser.ok(vos);
    }

}