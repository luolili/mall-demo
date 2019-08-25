package com.mall.search.pojo;

import com.mall.common.vo.PageResult;
import com.mall.item.pojo.Brand;
import com.mall.item.pojo.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
public class SearchResult extends PageResult<Goods> {

    List<Category> categoryList;//category
    List<Brand> brandList;//brand
    List<Map<String, Object>> specs;//specs

    public SearchResult() {

    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items,
                        List<Category> categoryList, List<Brand> brandList,
                        List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specs = specs;
    }
}
