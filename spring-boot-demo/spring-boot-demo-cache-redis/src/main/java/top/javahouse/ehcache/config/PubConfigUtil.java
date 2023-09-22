package top.javahouse.ehcache.config;

import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
;
import java.util.Objects;

/**
 *  启动项目后, 加载数据库公共配置数据到redis中
 *  在项目中不可避免的会存在像字典、部门这样使用频率很大的数据，如果每次都从数据库中查取数据，势必会增加项目的负担，所以很多开源的后台管理系统都集成了redis。
 * 这里记录一下自己对于将数据加载到redis使用的过程。
 * 首先项目启动时加载：这里使用了一个Java注解@PostConstruct@PostConstruct该注解被用来修饰一个非静态的void（）方法。
 * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
 * */
@Component
@Order(100)
public class PubConfigUtil {

    private static String pre = "pub_config"; // 存储到redis中的前缀
    private static String operation = ":"; // redis冒号分组

   /* @Resource
    private JdbcTemplate jdbcTemplate; // springboot集成mybatis-spring-boot-starter后本身封装的工具类
  */
    @Resource
    private RedisTemplate redisTemplate;

    public PubConfigUtil(){
        System.out.println("PubConfigUtil我先初始化");
    }

    @PostConstruct // 是java注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。
    public void reload() {
         /*String sql = "SELECT config_id,config_block,config_name,config_value,config_desc FROM ad_basic_config WHERE data_status = '1'"; // 数据库配置表,根据你自己的配置表定义查询语句
        List<Map<String, Object>> mapsList = jdbcTemplate.queryForList(sql);
         if (!CollectionUtils.isEmpty(mapsList)) { // 存在值
            StringBuilder sbl = new StringBuilder();
            for (Map<String, Object> map : mapsList) {
                sbl.setLength(0);
                String config_block = map.get("config_block").toString(); // 数据库配置表中的字段名称| 模块: aliyun
                String config_name = map.get("config_name").toString(); // 数据库配置表中的字段名称| 配置名: SMS_TEMPLATECODE_LOGGIN (阿里云下短信模板号 - 登录)
                String config_value = map.get("config_value").toString(); // 数据库配置表中的字段名称| 配置具体值: xxxxx (阿里云下短信模板号)
                String key = sbl.append(pre).append(operation).append(config_block).append(operation).append(config_name).toString();
                redisTemplate.opsForValue().set(key, config_value);
            }
        }
        */
        Object data= redisTemplate.opsForValue().get("commandLineRunner");
        if(Objects.isNull(data)){
            redisTemplate.opsForValue().set("commandLineRunner","CommandLineRunner");
            System.out.println("Spring Boot 启动以后然后再加载缓存数据 PubConfigUtil");
        }
        System.out.println("进入PubConfigUtil");

    }


}