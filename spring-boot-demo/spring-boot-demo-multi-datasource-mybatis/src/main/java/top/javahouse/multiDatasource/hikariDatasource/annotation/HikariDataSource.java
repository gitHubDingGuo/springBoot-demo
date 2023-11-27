package top.javahouse.multiDatasource.hikariDatasource.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface HikariDataSource {
    String name() default "";
}
