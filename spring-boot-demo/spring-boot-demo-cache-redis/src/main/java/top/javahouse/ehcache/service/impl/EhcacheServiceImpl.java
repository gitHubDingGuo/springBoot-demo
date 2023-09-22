package top.javahouse.ehcache.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.javahouse.ehcache.entity.Book;
import top.javahouse.ehcache.service.EhcacheService;

import java.util.*;

@Service
public class EhcacheServiceImpl implements EhcacheService {

    //public static Map<Long, String> articleMap = new HashMap<>();
    public static Map<Long, Book> articleMap = new HashMap<>();

    static {
        /*articleMap.put(1L, "文章1");
        articleMap.put(2L, "文章2");
        articleMap.put(3L, "文章3");*/
        for(int i=1;i<=3;i++){
            Book book=new Book();
            book.setId((long) i);
            book.setName("文章"+i);
            articleMap.put((long)i,book);
        }
    }

    //@Cacheable 作用: 在方法执行前，spring先查看缓存中是否有数据，如果有数据，则直接返回缓存数据；若没有数据，调用方法并将方法返回值放到缓存中
    @Override
    @Cacheable(value = "dataCache", key = "'getById'+#id")
    public Book getById(Long id) {
        System.out.println("获取来自数据库的文章:" + id);
        return this.articleMap.get(id);
    }


    //condition属性默认为空，表示将缓存所有的调用情形，其值是通过spel表达式来指定的，
            //当为true时表示先尝试从缓存中获取；若缓存中不存在，则只需方法，并将方法返回值丢到缓存中；
            //当为false的时候，不走缓存、直接执行方法、并且返回结果也不会丢到缓存中。
    //unless : 表示满足条件则不缓存 ; 与上述的condition是反向的 ;
    @Override
    @Cacheable(value = "dataCache", key = "'getById'+#id", condition = "#isTrue")
    public Book getByIdCondition(Long id,boolean isTrue) {
        System.out.println("获取来自数据库的文章:" + id);
        return this.articleMap.get(id);
    }


    @Cacheable(value = "dataCache", key = "'getById'+#id", unless = "#isTrue")
    public Book getByIdUnless(Long id,boolean isTrue) {
        System.out.println("获取来自数据库的文章:" + id);
        return this.articleMap.get(id);
    }


    //将结果放入缓存
    @CachePut(value = "dataCache", key = "'getById'+#id")
    public Book add(Long id, String content) {
        System.out.println("新增文章:" + id);
        //this.articleMap.put(id, content);
        return null;
    }

    // * value：缓存的名称，每个缓存名称下面可以有多个key
    //* key：缓存的key
    //删除
    @CacheEvict(value = "dataCache", key = "'getById'+#id")
    public void delete(Long id) {
        System.out.println("删除文章：" + id);
        this.articleMap.remove(id);
    }
    /*https://blog.csdn.net/doubleedged/article/details/125922281
    https://blog.csdn.net/qq_37493556/article/details/120934545
    https://blog.csdn.net/i402097836/article/details/114977864
    https://blog.csdn.net/zhaoyinjun7897/article/details/119862220
    */


}
