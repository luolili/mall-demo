package com.mall.order.serice;

import com.mall.common.utils.CodeMsg;
import com.mall.common.utils.Result;
import com.mall.order.client.MiaoshaGoodsClient;
import com.mall.order.dto.MiaoshaOrderDTO;
import com.mall.order.mapper.MiaoshaOrderMapper;
import com.mall.order.pojo.MiaoshaOrder;
import com.mall.order.sender.MiaoshaMsg;
import com.mall.order.sender.OrderSender;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MiaoshaOrderService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private MiaoshaGoodsClient miaoshaGoodsClient;
    @Resource
    private MiaoshaOrderMapper miaoshaOrderMapper;

    @Resource
    private OrderSender sender;
    private final String PLACE_ORDER_TOKEN_PREFIX = "place_order_token_user_id_";

    public Result createOrder(MiaoshaOrderDTO orderDTO, String token) {
        //防止重复下单
        Long userId = orderDTO.getUserId();
        boolean checkToken = checkToken(userId, token);
        if (!checkToken) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        // redis 减库存
        Long goodsId = orderDTO.getGoodsId();
        Integer buyNum = orderDTO.getBuyNum();
        miaoshaGoodsClient.decrStock(goodsId, buyNum);
        // 下单
        String orderNo = "";
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setOrderNo(orderNo);
        miaoshaOrder.setGoodsId(goodsId);
        miaoshaOrder.setUserId(userId);
        miaoshaOrder.setBuyNum(orderDTO.getBuyNum());
        miaoshaOrder.setGoodsName(orderDTO.getGoodsName());
        int count = miaoshaOrderMapper.insert(miaoshaOrder);
        if (count != 1) {
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }

        //支付

        // mq异步扣减库存
        MiaoshaMsg msg = new MiaoshaMsg();
        msg.setUserId(userId);
        msg.setGoodsId(goodsId);
        msg.setBuyNum(buyNum);
        sender.sendMiaoshaMsg(msg);
        return Result.success();
    }

    public boolean checkToken(Long userId, String token) {
        String key = PLACE_ORDER_TOKEN_PREFIX + userId;
        String redisToken = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(redisToken) || StringUtils.isBlank(token)) {
            return false;
        }
        if (StringUtils.equals(token, redisToken)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    public Result createToken(Long userId) {
        String key = PLACE_ORDER_TOKEN_PREFIX + userId;
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, token, 5, TimeUnit.SECONDS);
        return Result.success(token);
    }
}
