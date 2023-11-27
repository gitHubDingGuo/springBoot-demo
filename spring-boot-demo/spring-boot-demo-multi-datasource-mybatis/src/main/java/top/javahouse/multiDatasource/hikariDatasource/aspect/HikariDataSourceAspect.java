package top.javahouse.multiDatasource.hikariDatasource.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import top.javahouse.multiDatasource.druidDatasource.annotation.DataSource;
import top.javahouse.multiDatasource.druidDatasource.config.DynamicDataSource;
import top.javahouse.multiDatasource.druidDatasource.config.DynamicDataSourceConfig;
import top.javahouse.multiDatasource.hikariDatasource.annotation.HikariDataSource;
import top.javahouse.multiDatasource.hikariDatasource.config.HikariDatasourceConfig;
import top.javahouse.multiDatasource.hikariDatasource.config.MyHikariDataSource;

import java.lang.reflect.Method;

/**
 * 多数据源，切面处理类
 */
@Aspect
@Component
public class HikariDataSourceAspect implements Ordered {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(top.javahouse.multiDatasource.hikariDatasource.annotation.HikariDataSource)")
    public void hikariDataSourcePointCut() {

    }

    @Around("hikariDataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        HikariDataSource ds = method.getAnnotation(HikariDataSource.class);
        if (ds == null) {
            MyHikariDataSource.setDataSource(HikariDatasourceConfig.DataSourceNames.HIKARI_DEFAULT);
            logger.debug("set datasource is " + HikariDatasourceConfig.DataSourceNames.HIKARI_DEFAULT);
        } else {
            MyHikariDataSource.setDataSource(ds.name());
            logger.debug("set datasource is " + ds.name());
        }
        try {
            return point.proceed();
        } finally {
            MyHikariDataSource.clearDataSource();
            logger.debug("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
