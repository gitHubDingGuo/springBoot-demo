package top.javahouse.redis.redission;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class redissonConfig {

    @Bean
    public RedissonClient configRedisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
        config.setCodec(new StringCodec());
        //设置看门狗的时间，不配置的话默认30000
        //config.setLockWatchdogTimeout(12000);
        RedissonClient redisson =  Redisson.create(config);
        return redisson;
    }
}
