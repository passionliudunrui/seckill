package com.xxxx.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSenderTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    //单一的生产者
//    public void send(Object msg){
//        log.info("发送消息"+msg);
//        rabbitTemplate.convertAndSend("queue",msg);
//
//    }

    //广播模型的生产者
    public void send01(Object msg){
        log.info("发送消息"+msg);
        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
    }

    //direct模型

    public void send02(Object msg){
        log.info("发送消息"+msg);
        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
    }

    public void send03(Object msg){
        log.info("发送消息"+msg);
        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);

    }

}
