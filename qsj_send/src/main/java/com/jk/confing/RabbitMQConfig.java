package com.jk.confing;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // 标识此类为配置文件类
public class RabbitMQConfig {

    // 创建队列
    @Bean
    public Queue queueMessage(){
        return new Queue("1809a-user");
    }

}
