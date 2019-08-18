package com.mall.item.pojo;

import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 表的垂直拆分：提高查询效率
 * spu
 * 多是int类型；
 * spu_detail：
 * 多少字符串/文本
 * <p>
 * 增加商品的步骤：
 * 1. 基本信息：spu:分类，品牌，标题，商品卖点；packing list,after service
 * 再选品牌的时候，根据选择的分类来查询这个分类下面的所以品牌；
 * <p>
 * 2.商品描述
 * 3.规格参数：
 * 4.sku
 * 前台请求是json字符串, 不是form表单
 */
@Table(name = "tb_spu_detail")
@Data
public class SpuDetail {

    @Id
    private Long spuId;
    private String description;
    private String genericSpec;
    private String specialSpec;
    private String packingList;
    private String afterService;


}
