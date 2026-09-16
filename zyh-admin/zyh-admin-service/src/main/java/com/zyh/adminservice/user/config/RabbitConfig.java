package com.zyh.adminservice.user.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author zhangyuheng
 */
@Component
public class RabbitConfig {
    // 交换机的名称
    public final static String EXCHANGE_NAME = "edit_user_exchange";

    /**
     * 广播交换机
     * @return
     */
    @Bean
    public FanoutExchange editUserExchange() {
        return new FanoutExchange(EXCHANGE_NAME, true, true);
    }
}
