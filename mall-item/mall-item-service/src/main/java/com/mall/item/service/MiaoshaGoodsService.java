package com.mall.item.service;

import com.mall.common.utils.CodeMsg;
import com.mall.common.utils.Result;
import com.mall.item.mapper.MiaoshaGoodsMapper;
import com.mall.item.pojo.MiaoshaGoods;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MiaoshaGoodsService implements InitializingBean {
    // 内存标记
    private Map<Long, Boolean> isOverMap = new HashMap<>();
    @Autowired
    private MiaoshaGoodsMapper miaoshaGoodsMapper;
    private static final String KEY_PREFIX = "goodsId_";
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;


    /**
     * redis 预减库存
     *
     * @param goodsId
     * @param buyNum
     * @return
     */
    public Result decrStockByRedis(Long goodsId, int buyNum) {
        // 先从内存标记里判断
        Boolean isOver = isOverMap.get(goodsId);
        if (isOver) {
            return Result.error(CodeMsg.MIAOSHA_STOCK_NOT_ENOUGH);
        }
        //Integer stock = redisTemplate.opsForValue().get(KEY_PREFIX + goodsId);
        Integer stock = (Integer) redisTemplate.boundHashOps("seckillGood").get(KEY_PREFIX + goodsId);
        if (stock == 0) {
            isOverMap.put(goodsId, true);
        }
        if (stock < buyNum) {
            return Result.error(CodeMsg.MIAOSHA_STOCK_NOT_ENOUGH);
        }
        Long count = redisTemplate.opsForValue().decrement(KEY_PREFIX + goodsId, buyNum);
        if (count < 0) {
            return Result.error(CodeMsg.MIAOSHA_STOCK_DECR_ERROR);
        }
        return Result.success();
    }

    /**
     * 扣减库存，用数据库悲观锁，并发小
     *
     * @param goodsId
     * @param buyNum
     * @return
     */
    public Result decrStockByMysql(Long goodsId, int buyNum) {
        Example example = new Example(MiaoshaGoods.class);
        Example.Criteria criteria = example.createCriteria();
        // 查询该秒杀商品的库存
        MiaoshaGoods miaoshaGoods = miaoshaGoodsMapper.selectByPrimaryKey(goodsId);

        if (miaoshaGoods == null) {
            return Result.error(CodeMsg.MIAOSHA_GOODS_NOT_EXIST);
        }
        Integer stock = miaoshaGoods.getStock();
        if (stock < buyNum) {
            return Result.error(CodeMsg.MIAOSHA_STOCK_NOT_ENOUGH);
        }
        miaoshaGoods.setStock(stock - buyNum);
        int count = miaoshaGoodsMapper.updateByPrimaryKey(miaoshaGoods);
        if (count != 1) {
            return Result.error(CodeMsg.MIAOSHA_STOCK_DECR_ERROR);
        }
        return Result.success("");
    }

    /**
     * 把商品库存余下放入redis
     *
     * @return
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<MiaoshaGoods> list = miaoshaGoodsMapper.selectAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (MiaoshaGoods goods : list) {
                // 方法1
                // redisTemplate.opsForValue().set(KEY_PREFIX+goods.getId(), goods.getStock());
                //方法2：用 hash
                redisTemplate.boundHashOps("seckillGoods").put(goods.getId(), goods.getStock());
                isOverMap.put(goods.getId(), false);
            }
        }
    }
}