package com.mall.item.mapper;


import com.mall.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    //中间表的保存
    @Insert("insert tb_category_brand (category_id, brand_id) values (#{cid}, #{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("select b.* from tb_category_brand cb inner join tb_brand b on b.id=cb.brand_id " +
            "where cb.category_id = #{cid}")
    List<Brand> queryBrandListByCid(@Param("cid") Long cid);
}
