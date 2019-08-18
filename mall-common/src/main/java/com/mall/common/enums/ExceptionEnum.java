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
    BRAND_NOT_FOUND(405,"品牌没查到"),
    BRAND_SAVE_ERROR(500,"品牌保存出错"),
    UPLOAD_ERROR(500,"文件上传出错"),
    INVALID_FILE_TYPE(400,"无效的文件类型"),
    ;

    private int code;
    private String msg;
}
