
package top.javahouse.multiDatasource.hikariDatasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源
 */

/*（1）此类是用来配置动态数据源的关键。该类继承spring的抽象类AbstractRoutingDataSource，
      通过这个类可以实现动态数据源切换。其中维护着默认数据源（defaultTargetDataSource）和
      数据源列表（targetDataSources），通过afterPropertiesSet()方法对数据源列表进行解析以及设置数据源。
        程序每次对数据库发起连接时，都会访问到AbstractRoutingDataSource的getConnection()方法，
        此方法会调用determineCurrentLookupKey的相应实现，此处实现为获取线程变量。
        版权声明：本文为CSDN博主「@Ciano」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/qq1017794482/article/details/128485480*/
public class MyHikariDataSource extends AbstractRoutingDataSource {
    //添加DynamicDataSourceContextHolder类，此类用于切换数据源，
    //根据ThreadLocal做多线程数据隔离，每一次切换都能保证不影响其他线程的正常运行
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public MyHikariDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

  /*
     AbstractRoutingDataSource切换线程变量来切换数据源源码。
    此处就是获取之前在setDataSource中set到线程中的数据源名，
    通过数据源名获取维护的数据源列表中对应的数据源
*/

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }

}
