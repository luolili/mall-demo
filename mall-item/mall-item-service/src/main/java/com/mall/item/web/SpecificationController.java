package com.mall.item.web;

import com.mall.item.pojo.SpecGroup;
import com.mall.item.pojo.SpecParam;
import com.mall.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网关访问：/api/item是前缀
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    //根据商品分类查询规格组
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupListByCid(
            @PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specificationService.queryGroupListByCid(cid));
    }

    //根据商规格组id, 分类id，是否搜索 查询规格参数集合
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamListByGid(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching) {
        return ResponseEntity.ok(specificationService.queryParamList(gid, cid, searching));
    }

}
