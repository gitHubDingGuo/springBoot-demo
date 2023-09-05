package top.javahouse.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/*
 * @author:javahouse.top
 * @Description: 任务工厂
 * @Date: 2023/9/5 15:51
 */
@Component
@Slf4j
public class TaskFactory {

    @Async
    public Future<Boolean> doSomething(){
        //TODO 模拟睡5秒
        try {
            System.out.println(Thread.currentThread().getName()+"-----处理开始------");
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName()+"-----处理结束------");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AsyncResult<>(Boolean.FALSE);
        }
        return new AsyncResult<>(Boolean.TRUE);
    }

    @Async("poolOne")
    public Future<Boolean> poolOne(){
        //TODO 模拟睡5秒
        try {
            System.out.println(Thread.currentThread().getName()+"-----poolOne处理开始------");
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName()+"-----poolOne处理结束------");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AsyncResult<>(Boolean.FALSE);
        }
        return new AsyncResult<>(Boolean.TRUE);
    }


    @Async("poolTwo")
    public Future<Boolean> poolTwo(){
        //TODO 模拟睡5秒
        try {
            System.out.println(Thread.currentThread().getName()+"-----poolTwo处理开始------");
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName()+"-----poolTwo处理结束------");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AsyncResult<>(Boolean.FALSE);
        }
        return new AsyncResult<>(Boolean.TRUE);
    }

}
