package com.mall.item.client;

import com.mall.item.pojo.SpecGroup;
import com.mall.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    // 根据分类id查询SpecGroup集合
    @GetMapping("spec/groups")
    public List<SpecGroup> queryGroupListByGid(
            @RequestParam(value = "cid", required = false) Long cid);

    @GetMapping("spec/params")
    List<SpecParam> queryParamListByGid(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching);
}
