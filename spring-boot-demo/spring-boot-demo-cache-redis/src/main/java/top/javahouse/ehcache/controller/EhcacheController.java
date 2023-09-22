package top.javahouse.ehcache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.ehcache.service.EhcacheService;


@RestController
public class EhcacheController {

    @Autowired
    private EhcacheService ehcacheService;

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("getById")
    public ResponseEntity<?> getById() {
        System.out.println(ehcacheService.getById(1L));
        //第二次会取缓存，如果第一次缓存成功了
        System.out.println(ehcacheService.getById(1L));
        return ResponseEntity.ok(null);
    }

    @GetMapping("getByIdCondition")
    public ResponseEntity<?> getByIdCondition() {
        //当为true时表示先尝试从缓存中获取；若缓存中不存在，则只需方法，并将方法返回值丢到缓存中；
        System.out.println(ehcacheService.getByIdCondition(1L,true));
        //直接获取缓存数据
        System.out.println(ehcacheService.getById(1L));
        //删除数据
        ehcacheService.delete(1L);
        //不走缓存、直接执行方法、并且返回结果也不会丢到缓存中
        System.out.println(ehcacheService.getByIdCondition(2L,false));
        System.out.println(ehcacheService.getByIdCondition(2L,false));
        return ResponseEntity.ok(null);
    }

    @GetMapping("getByIdUnless")
    public ResponseEntity<?> getByIdUnless()  {
        //当为true时不走缓存、直接执行方法、并且返回结果也不会丢到缓存中
        System.out.println(ehcacheService.getByIdUnless(1L,true));
        //当为false时表示先尝试从缓存中获取；缓存存在直接返回，若缓存中不存在，则只需方法，并将方法返回值丢到缓存中；
        System.out.println(ehcacheService.getByIdUnless(1L,false));
        //这一次取缓存
        System.out.println(ehcacheService.getByIdUnless(1L,false));
        try {
            Thread.sleep(10*1000);
            ehcacheService.delete(1L);
        }catch (Exception e){}
        return ResponseEntity.ok(null);
    }


    @GetMapping("add")
    public ResponseEntity<?> add() {
        /*被标注的方法每次都会被调用，然后方法执行完毕之后，会将方法结果丢到缓存中；当标注在类上，相当于在类的所有方法上标注了@CachePut。
        有3种情况，结果不会丢到缓存当方法向外抛出的时候condition的计算结果为false的时候unless的计算结果为true的时候*/
        //新增文章，由于add方法上面有@CachePut注解，所以新增之后会自动丢到缓存中
        ehcacheService.add(4L, "文章4");
        System.out.println(ehcacheService.getById(4L));
        //相同的key，缓存会更新内容
        ehcacheService.add(4L, "文章444444");
        System.out.println(ehcacheService.getById(4L));
        return ResponseEntity.ok(null);
    }


    @GetMapping("delete")
    public ResponseEntity<?> delete() {
        //执行删除操作，delete方法上面有@CacheEvict方法，会清除缓存
        ehcacheService.delete(1L);
        return ResponseEntity.ok(null);

    }

}
