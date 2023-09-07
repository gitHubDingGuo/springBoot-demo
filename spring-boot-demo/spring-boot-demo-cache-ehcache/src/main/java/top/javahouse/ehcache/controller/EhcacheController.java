package top.javahouse.ehcache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.ehcache.service.EhcacheService;


@RestController
public class EhcacheController {

    @Autowired
    private EhcacheService ehcacheService;

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("getList")
    public ResponseEntity<?>  getList(){
        System.out.println(ehcacheService.getList());
        System.out.println(ehcacheService.getList());
        return ResponseEntity.ok(null);
    }

    @GetMapping("getPage")
    public ResponseEntity<?> getPage(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
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
        return ResponseEntity.ok(null);
    }

    @GetMapping("getById")
    public ResponseEntity<?> getById(){
        System.out.println(ehcacheService.getById(1L, true));
        System.out.println(ehcacheService.getById(1L, true));
        System.out.println(ehcacheService.getById(1L, false));
        System.out.println(ehcacheService.getById(1L, true));
        return ResponseEntity.ok(null);
    }

    @GetMapping("findById")
    public ResponseEntity<?> findById(){
        System.out.println(ehcacheService.findById(1L));
        System.out.println(ehcacheService.findById(1L));
        System.out.println(ehcacheService.findById(3L));
        System.out.println(ehcacheService.findById(3L));
        {
            System.out.println("下面打印出缓cache1缓存中的key列表");
            ConcurrentMapCacheManager cacheManager = applicationContext.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
        return ResponseEntity.ok(null);
    }


    @GetMapping("add")
    public ResponseEntity<?> add(){
        //新增3个文章，由于add方法上面有@CachePut注解，所以新增之后会自动丢到缓存中
        ehcacheService.add(1L, "java高并发系列");
        ehcacheService.add(2L, "Maven高手系列");
        ehcacheService.add(3L, "MySQL高手系列");

        //然后调用findById获取，看看是否会走缓存
        System.out.println("调用findById方法，会尝试从缓存中获取");
        System.out.println(ehcacheService.findById(1L));
        System.out.println(ehcacheService.findById(2L));
        System.out.println(ehcacheService.findById(3L));

        {
            System.out.println("下面打印出cache1缓存中的key列表");
            ConcurrentMapCacheManager cacheManager = applicationContext.getBean(ConcurrentMapCacheManager.class);
            ConcurrentMapCache cache1 = (ConcurrentMapCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
        return ResponseEntity.ok(null);
    }



    @GetMapping("delete")
    public ResponseEntity<?> delete() {
        //第1次调用findById，缓存中没有，则调用方法，将结果丢到缓存中
        System.out.println(ehcacheService.findById(1L));
        //第2次调用findById，缓存中存在，直接从缓存中获取
        System.out.println(ehcacheService.findById(1L));

        //执行删除操作，delete方法上面有@CacheEvict方法，会清除缓存
        ehcacheService.delete(1L);

        //再次调用findById方法，发现缓存中没有了，则会调用目标方法
        System.out.println(ehcacheService.findById(1L));
        return ResponseEntity.ok(null);

    }








}
