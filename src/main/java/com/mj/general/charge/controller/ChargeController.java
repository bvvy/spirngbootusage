package com.mj.general.charge.controller;

import com.mj.core.data.del.SingleId;
import com.mj.core.data.resp.Messager;
import com.mj.core.er.Responser;
import com.mj.general.charge.dto.ChargeAddDTO;
import com.mj.general.charge.dto.ChargeCheckDTO;
import com.mj.general.charge.dto.ChargeQueryDTO;
import com.mj.general.charge.dto.ChargeUpdateDTO;
import com.mj.general.charge.model.Charge;
import com.mj.general.charge.service.ChargeService;
import com.mj.general.charge.vo.ChargeVO;
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
 * @author zejun
 * @date 2018/7/10 09:03
 * 费用项目管理
 */
@RestController
@RequestMapping("/v1/charge")
@Api(tags = "费用项目管理")
public class ChargeController {

    private final ChargeService chargeService;

    public ChargeController(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @PostMapping
    @ApiOperation("新增费用")
    public ResponseEntity<Messager> add(@Valid @RequestBody ChargeAddDTO chargeAddDTO, BindingResult br) {
        //todo 查询登陆人信息获取客户公司id
        int companyId = 1;
        Charge charge = Charge.builder().chargeItemCode(chargeAddDTO.getChargeItemCode())
                .chargeItemCN(chargeAddDTO.getChargeItemCN())
                .chargeItemEN(chargeAddDTO.getChargeItemEN())
                .companyId(companyId).build();
        chargeService.add(charge);
        return Responser.created();
    }

    @PatchMapping
    @ApiOperation("修改费用")
    public ResponseEntity<Messager> update(@Valid @RequestBody ChargeUpdateDTO chargeUpdateDTO, BindingResult br) {
        Charge charge = chargeService.get(chargeUpdateDTO.getId());
        charge.setChargeItemCode(chargeUpdateDTO.getChargeItemCode());
        charge.setChargeItemCN(chargeUpdateDTO.getChargeItemCN());
        charge.setChargeItemEN(chargeUpdateDTO.getChargeItemEN());
        chargeService.update(charge);
        return Responser.updated();
    }

    @PatchMapping("/enabled")
    @ApiOperation("禁用/启用操作")
    public ResponseEntity<Messager> enabled(@Valid @RequestBody SingleId del, BindingResult br){
        chargeService.toggleEnable(del.getId());
        return Responser.updated();
    }

    @GetMapping("/find")
    @ApiOperation("费用分页查询")
    public ResponseEntity<Page<ChargeVO>> find(ChargeQueryDTO chargeQueryDTO, Pageable pageable) {
        Page<Charge> charges = chargeService.find(chargeQueryDTO, pageable);
        Page<ChargeVO> page = charges.map(charge -> ChargeVO.builder()
                .id(charge.getId())
                .chargeItemCode(charge.getChargeItemCode())
                .chargeItemCN(charge.getChargeItemCN())
                .chargeItemEN(charge.getChargeItemEN())
                .enabled(charge.getEnabled()).build());
        return ResponseEntity.ok(page);
    }

    @GetMapping("/all")
    @ApiOperation("费用全部查询")
    public ResponseEntity<List<ChargeVO>> all() {

        List<Charge> all = chargeService.findAll();
        List<ChargeVO> vos = all.stream().map(charge -> ChargeVO.builder()
                .id(charge.getId())
                .chargeItemCode(charge.getChargeItemCode())
                .chargeItemCN(charge.getChargeItemCN())
                .chargeItemEN(charge.getChargeItemEN())
                .enabled(charge.getEnabled()).build()).collect(Collectors.toList());
        return Responser.ok(vos);
    }

    @GetMapping("/check")
    @ApiOperation("检查字段是否重复")
    public void check(ChargeCheckDTO chargeCheckDTO) {
        chargeService.check(chargeCheckDTO);
    }

}
