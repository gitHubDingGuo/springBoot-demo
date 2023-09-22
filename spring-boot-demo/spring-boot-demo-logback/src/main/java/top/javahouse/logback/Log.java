package top.javahouse.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * @author:javahouse.top
 * @Description: 打印日志
 * @Date: 2023/9/22 13:43
 */
@Component
public class Log {

    Logger logger= LoggerFactory.getLogger(Log.class);

    public Log(){
        logger.info("日志={}","logback");
    }
}
