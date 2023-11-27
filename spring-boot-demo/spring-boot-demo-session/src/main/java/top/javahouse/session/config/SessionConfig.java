package top.javahouse.session.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600,redisNamespace = "my:spring:session")
/*@EnableRedisHttpSession(redisNamespace = "bjsxt:session", maxInactiveIntervalInSeconds = 3600,
        flushMode = FlushMode.ON_SAVE, saveMode = SaveMode.ON_SET_ATTRIBUTE,
        cleanupCron = "0 * * * * *"
)
（1）redisNamespace

保存在redis中的数据的key前缀。默认spring:session。

（2）maxInactiveIntervalInSeconds

会话生命周期。默认1800秒。

（3）flushMode

刷新redis数据的模式。默认ON_SAVE。在保存时才刷新。另一个取值FlushMode.IMMEDIATE，不刷新。

（4）saveMode

保存模式。默认值SaveMode.ON_SET_ATTRIBUTE：在设置session属性时保存。可选值如下：

SaveMode.ALWAYS：实时保存。

SaveMode.ON_GET_ATTRIBUTE：获取值时才进行保存到Redis。

（5）cleanupCron

清除Redis中数据的表达式。默认”0 * * * * *”

 五、Spring Session保存在Redis中的数据
（1）spring:session:sessions:唯一值

value的类型是Hash类型。表示整个Session对象。包括：

creationTime ：创建会话的时间

lastAccessedTime ：最后一次访问时间

maxInactiveInterval ：有效生命周期，默认30分钟

sessionAttr:xxx ：会话中的attribute值。field中的xxx代表会话中attribute的名字，会话中每个attribute，对应一个field-value对。

 （2）spring:session:sessions:expires:唯一值

value的类型是String类型。没有什么特殊内容。表示当前session过期状态。如果没有该属性说明，当前Session已过期。

（3）spring:session:erxpirations:时间戳

value的类型是Set类型。里面存储了这个时间戳是哪个session的。如果过期了，该键值对也会被删除掉。

————————————————
 原文链接：https://blog.csdn.net/weixin_53455615/article/details/128587358*/
public class SessionConfig {

}
