package com.mall.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.mall.common.vo.PageResult;
import com.mall.item.mapper.BrandMapper;
import com.mall.item.mapper.SpecGroupMapper;
import com.mall.item.mapper.SpecParamMapper;
import com.mall.item.pojo.Brand;
import com.mall.item.pojo.SpecGroup;
import com.mall.item.pojo.SpecParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;


    public List<SpecGroup> queryGroupListByCid(Long cid) {

        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(group);

        if (CollectionUtils.isEmpty(list)) {
            throw new MallException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }

        return list;
    }

    public List<SpecParam> queryParamList(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)) {
            throw new MallException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    public List<SpecGroup> queryGroupListByGid(Long cid) {
        List<SpecGroup> list = queryGroupListByCid(cid);
        //先把分类下的所以规格参数查出来
        List<SpecParam> specParams = queryParamList(null, cid, null);

        //构造map: key:  geoup id--value:list<param>
        Map<Long, List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : specParams) {
            if (map.containsKey(param.getGroupId())) {
                List<SpecParam> arrayList = new ArrayList<>();
                map.put(param.getGroupId(), arrayList);
            }
            map.get(param.getGroupId()).add(param);
        }
        for (SpecGroup group : list) {
            group.setParamList(map.get(group.getId()));
        }
        return list;
    }
}
