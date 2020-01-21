package com.mall.listener;

import com.mall.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 邮件发送和注册解耦；spring会获取 ApplicationListener 的所有实现类，并且调用listener
 * 的 onApplicationEvent()
 */
@Slf4j
@Component
//异步处理，这是单应用/不复杂的可以采用该方法，复杂的：mq
@Async
public class ToEmailListener implements ApplicationListener<UserRegisterEvent> {
    @Override
    public void onApplicationEvent(UserRegisterEvent event) {
        log.info("开始 发送邮件", event.getEmail());
        try {
            Thread.sleep(10);
        } catch (Exception e) {

        }
        log.info(" 发送邮件 结束", event.getEmail());
    }
}
