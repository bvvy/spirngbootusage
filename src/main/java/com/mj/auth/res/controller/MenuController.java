package com.mj.auth.res.controller;

import com.mj.auth.res.dto.MenuAddDTO;
import com.mj.auth.res.dto.MenuUpdateDTO;
import com.mj.auth.res.model.Menu;
import com.mj.auth.res.service.MenuService;
import com.mj.auth.res.service.OperService;
import com.mj.auth.res.vo.MenuVO;
import com.mj.core.data.del.SingleDelete;
import com.mj.core.data.resp.Messager;
import com.mj.core.data.tree.Tree;
import com.mj.core.er.Responser;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author bvvy
 * 菜单contorller
 */
@RestController
@RequestMapping("/v1/menu")
public class MenuController {

    private final MenuService menuService;
    private final OperService operService;

    public MenuController(MenuService menuService,
                          OperService operService) {
        this.menuService = menuService;
        this.operService = operService;

    }

    @PostMapping
    public ResponseEntity<Messager> add(@Valid @RequestBody MenuAddDTO menuAddDTO, BindingResult br) {
        menuService.add(menuAddDTO);
        return Responser.created();
    }

    @PatchMapping
    public ResponseEntity<Messager> update(@Valid @RequestBody MenuUpdateDTO menuUpdateDTO, BindingResult br) {
        Menu menu = menuService.get(menuUpdateDTO.getId());
        menu.setEnabled(menuUpdateDTO.getEnabled());
        menu.setName(menuUpdateDTO.getName());
        menu.setPath(menuUpdateDTO.getPath());
        menu.setOrderNum(menuUpdateDTO.getOrderNum());
        menu.setPid(menuUpdateDTO.getPid());
        menu.setType(menuUpdateDTO.getType());
        menu.setCode(menuUpdateDTO.getCode());
        menuService.update(menu);
        return Responser.updated();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuVO> get(@PathVariable Integer id) {
        Menu menu = menuService.get(id);
        MenuVO menuVO = MenuVO.builder()
                .enabled(menu.getEnabled())
                .id(menu.getId())
                .name(menu.getName())
                .orderNum(menu.getOrderNum())
                .path(menu.getPath())
                .pid(menu.getPid())
                .type(menu.getType())
                .code(menu.getCode())
                .build();
        return Responser.ok(menuVO);

    }

    @DeleteMapping
    public ResponseEntity<Messager> delete(@Valid @RequestBody SingleDelete del,BindingResult br) {
        menuService.delete(del.getId());
        return Responser.deleted();
    }

    @GetMapping("/tree")
    public ResponseEntity<List<Tree>> tree() {
        return Responser.ok(menuService.findMenuTree());
    }

    @GetMapping("/user/tree")
    public ResponseEntity<List<Tree>> userTrees() {
        return Responser.ok(menuService.findLeftMenuTreeBySelf());
    }
}
