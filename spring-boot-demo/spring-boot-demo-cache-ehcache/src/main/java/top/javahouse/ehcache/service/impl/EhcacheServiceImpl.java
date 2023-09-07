package top.javahouse.ehcache.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.javahouse.ehcache.service.EhcacheService;

import java.util.*;

@Service
//@CacheConfig(cacheNames = "cache1") 公共抽取
public class EhcacheServiceImpl implements EhcacheService {

    Map<Long, String> articleMap = new HashMap<>();



    @Cacheable(cacheNames = {"cache1"})
    public List<String> getList() {
        System.out.println("---第一次进来获取列表数据---");
        return Arrays.asList("1", "2", "3", "4");
    }


    @Cacheable(cacheNames = {"cache1"}, key = "#root.target.class.name+'-'+#page+'-'+#pageSize")
    public String getPage(int page, int pageSize) {
        String msg = String.format("page-%s-pageSize-%s", page, pageSize);
        System.out.println("第一次从db中获取数据：" + msg);
        return msg;
    }

    @Override
    @Cacheable(cacheNames = "cache1", key = "'getById'+#id", condition = "#cache")
    public String getById(Long id, boolean cache) {
        System.out.println("获取数据!");
        return "spring缓存:" + UUID.randomUUID().toString();
    }

    /**
     * 获取文章，先从缓存中获取，如果获取的结果为空，不要将结果放在缓存中
     */
    @Cacheable(cacheNames = "cache1", key = "'findById'+#id", unless = "#result==null")
    public String findById(Long id) {
        this.articleMap.put(1L, "spring系列");
        System.out.println("----获取文章:" + id);
        return articleMap.get(id);
    }


    //将结果放入缓存
    @CachePut(cacheNames = "cache1", key = "'findById'+#id")
    public String add(Long id, String content) {
        System.out.println("新增文章:" + id);
        this.articleMap.put(id, content);
        return content;
    }

    //删除
    @CacheEvict(cacheNames = "cache1", key = "'findById'+#id")
    public void delete(Long id) {
        System.out.println("删除文章：" + id);
        this.articleMap.remove(id);
    }



}
