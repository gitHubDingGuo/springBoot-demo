package top.javahouse.redis.redission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Api(tags = "Redisson")
@RestController
public class RedissionController {

    @Autowired
    RedissonClient redisson;

    @ApiOperation("getWatchDogLock")
    @GetMapping(value = "/getWatchDogLock")
    public String getWatchDog() throws InterruptedException {
        RLock watchDogLock = redisson.getLock("watchDogLock");
        boolean judge;
        //进行3s的尝试时间，如果失败则返回false， 还可以设置锁过期时间，如果设置会导致看门狗无效
        judge = watchDogLock.tryLock(3,  TimeUnit.SECONDS);
        watchDogLock.tryLock(100,100,TimeUnit.MINUTES);
        //输出是否能够获取到锁
        System.out.println(Thread.currentThread().getName() + "的锁获取情况：" + judge);
        //如果获取到锁
        if (judge){
            try {
                System.out.println(Thread.currentThread().getName() + " 已经成功获取到的分布式锁" + System.currentTimeMillis());
                //执行主要业务逻辑
                Thread.sleep(45*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                //最后进行解锁操作，如果服务宕机，则到不了这一步，会等待看门狗监控锁结束锁清除
                System.out.println(Thread.currentThread().getName() + " 进行解锁操作" + System.currentTimeMillis());
                watchDogLock.unlock();
            }
        }else {
            //如果没有获取到分布式锁，执行业务逻辑
            System.out.println(Thread.currentThread().getName() + " 没有成功获取到分布式锁，锁已经被占用" + System.currentTimeMillis());
        }
        //最后输出
        if (judge){
            return "成功获取到分布式锁";
        }
        return "没有获取到分布式锁";
    }

}
