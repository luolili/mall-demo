package com.mall.user.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

public class RateLimiterFilter extends ZuulFilter {

    public static RateLimiter RATE_LIMITER = RateLimiter.create(10);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (RATE_LIMITER.tryAcquire()) {
            throw new MallException(ExceptionEnum.USER_PARAM_ERROR);
        }
        return null;
    }
}
