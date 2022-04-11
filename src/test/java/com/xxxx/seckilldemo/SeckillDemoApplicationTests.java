package com.xxxx.seckilldemo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@SuppressWarnings("all")
public class SeckillDemoApplicationTests {
    /**
     * 使用Redis实现分布式锁
     */

    @Autowired
    private RedisTemplate  redisTemplate;

    @Autowired
    private RedisScript<Boolean> script;

    @Test
    public void contextLoads(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //如果不存在这个key的话，那么就创建成功，如果存在这个key的话，那就创建失败。
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1",5, TimeUnit.SECONDS);
        //如果占位成功进行业务操作
        if(isLock){
            valueOperations.set("name","xxxx");
            String name = (String)valueOperations.get("name");
            //操作结束删除锁
            //业务处理完成，要删除这个key

            /*
            注意的问题，如果在这个过程中，程序抛出了异常的话，那么锁不可用，
            出现严重的问题
            解决方法：
            设置锁的过期时间

            新问题：如果这个时间段内，线程没有执行完成的话，那么就被删掉了这把锁
            解决方法：
            1.获取这把锁
            2.获取这把锁的value 是否和自己产生的value相同。
            3.删除锁

            新问题：这里必须是一个原子性的操作，如何保证原子性
            解决方法：
            1.通过使用lua脚本来实现

             */


            redisTemplate.delete("k1");
        }
        else{
            System.out.println("有其他线程再使用");;
        }


    }


    /**
     * 通过使用lua脚本解决不是原子性的问题
     * 1.通过先在服务器写好
     * 2.通过java传递到redis
     */

    @Test
    public void test02(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean isLock=valueOperations.setIfAbsent("k1","v1");
        String value= UUID.randomUUID().toString();
        if(isLock){
            valueOperations.set("name","xxxx");
            String name=(String)valueOperations.get("name");
            System.out.println("name"+name);
            System.out.println(valueOperations.get("k1"));

            Boolean result = (Boolean) redisTemplate.execute(script, Collections.singletonList("k1"),value);
            System.out.println(result);

        }
        else{
            System.out.println("有线程占用，请稍后重试");
        }



    }
}
