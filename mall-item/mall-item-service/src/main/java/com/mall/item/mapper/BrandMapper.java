package com.mall.item.mapper;


import com.mall.item.pojo.Brand;
import com.mall.item.pojo.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {

    //中间表的保存
    @Insert("insert tb_category_brand (category_id, brand_id) values (#{cid}, #{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);
}
