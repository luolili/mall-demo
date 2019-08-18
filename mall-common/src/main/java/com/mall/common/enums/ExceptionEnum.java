package com.mall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(404,"商品分类没查到"),
    SPU_DETAIL_NOT_FOUND(404, "商品详情没查到"),
    SKU_NOT_FOUND(404, "商品sku没查到"),
    STOCK_NOT_FOUND(404, "商品库存没查到"),
    SPEC_GROUP_NOT_FOUND(404, "商品规格组没查到"),
    SPEC_PARAM_NOT_FOUND(404, "规格参数没查到"),
    BRAND_NOT_FOUND(405,"品牌没查到"),
    GOODS_NOT_FOUND(405, "商品没查到"),
    BRAND_SAVE_ERROR(500,"品牌保存出错"),
    UPLOAD_ERROR(500,"文件上传出错"),
    INVALID_FILE_TYPE(400,"无效的文件类型"),
    GOODS_SAVE_ERROR(500, "保存商品失败"),
    ;

    private int code;
    private String msg;
}
