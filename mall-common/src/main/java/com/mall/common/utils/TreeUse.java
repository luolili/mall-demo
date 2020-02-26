package com.mall.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUse {

    public Result tree(List<GoodsType> list) {
        return Result.success(new JsonTreeCreater<GoodsType>(
                list,
                item -> item.getId(),
                a -> StringUtils.isEmpty(a.getParentId()) ? "0" : a.getParentId(),
                a -> a.getName(),
                StringUtils.isEmpty("") ? a -> false : a -> false,
                a -> {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("id", a.getId());
                    dataMap.put("product_name", a.getName());
                    return dataMap;
                }
        ).create());
    }
}
