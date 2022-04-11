package com.xxxx.seckill.config;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQTopicConfig {
    private static final String QUEUE="seckillQueue";
    private static final String ECCHANGE="seckillExchange";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(ECCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }

}
