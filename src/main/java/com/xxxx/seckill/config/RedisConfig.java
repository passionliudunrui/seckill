package com.xxxx.seckill.config;


import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String ,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<String,Object>redisTemplate=new RedisTemplate<>();
        //key的序列化方法
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //value设置序列化的方式
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        //设置hash类型的序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        //设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;

    }

    /**
     * 使用LUA脚本
     * @return
     */
//    @Bean
//    public DefaultRedisScript<Boolean>script(){
//        DefaultRedisScript<Boolean>redisScript=new DefaultRedisScript<>();
//
//        //lock.lua脚本位置和application在同一个目录
//        redisScript.setLocation(new ClassPathResource("lock.lua"));
//        redisScript.setResultType(Boolean.class);
//        return redisScript;
//    }

    /**
     * 项目中使用LUA脚本解决库存问题
     */
    @Bean
    public DefaultRedisScript<Long>script(){
        DefaultRedisScript<Long>redisScript=new DefaultRedisScript<>();

        //lock.lua脚本位置和application在同一个目录
        redisScript.setLocation(new ClassPathResource("stock.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }




}
