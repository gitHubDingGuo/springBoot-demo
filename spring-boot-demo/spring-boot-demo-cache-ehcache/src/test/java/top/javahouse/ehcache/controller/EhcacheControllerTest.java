package top.javahouse.ehcache.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import top.javahouse.ehcache.service.EhcacheService;

@SpringBootTest
//表明当前单元测试是运行在Spring Boot环境中的

class EhcacheControllerTest {

    @Autowired
    private EhcacheService ehcacheService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void getPage() {

        //page=1,pageSize=10调用2次
        System.out.println(ehcacheService.getPage(1, 10));
        System.out.println(ehcacheService.getPage(1, 10));

        //page=2,pageSize=10调用2次
        System.out.println(ehcacheService.getPage(2, 10));
        System.out.println(ehcacheService.getPage(2, 10));

        {
            System.out.println("下面打印出cache1缓存中的key列表");
            ConcurrentMapCacheManager cacheManager = applicationContext.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
    }
}