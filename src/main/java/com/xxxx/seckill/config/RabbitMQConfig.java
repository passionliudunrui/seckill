package com.xxxx.seckill.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //广播模型
    private static final String QUEUE01="queue_fanout01";
    private static final String QUEUE02="queue_fanout02";
    private static final String EXCHANGE="fanoutExchange";//广播模型

    //路由模型
    private static final String QUEUE03="queue_direct01";
    private static final String QUEUE04="queue_direct02";
    private static final String EXCHANGE1="directExchange";
    private static final String ROUTINGKEY01="queue.red";
    private static final String ROUTINGKEY02="queue.green";

    //路由模型的配置

    @Bean
    public Queue queue03(){
        return new Queue(QUEUE03);
    }

    @Bean
    public Queue queue04(){
        return new Queue(QUEUE04);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE1);
    }

    @Bean
    public Binding binding03(){
        return BindingBuilder.bind(queue03()).to(directExchange()).with(ROUTINGKEY01);
    }

    @Bean
    public Binding binding04(){
        return BindingBuilder.bind(queue04()).to(directExchange()).with(ROUTINGKEY02);
    }


//    @Bean
//    public Queue queue(){
//        return new Queue("queue",true);
//    }

    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }

    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }

    //广播模型

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE);
    }

    //队列绑定到交换机上面
    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(fanoutExchange());
    }

    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(fanoutExchange());
    }


}
