package top.javahouse.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * @author:javahouse.top
 * @Description:调用入口
 * @Date: 2023/9/5 15:50
 */
@Component
public class AsyncCaller {

    @Autowired
    TaskFactory taskFactory;

    public void caller() {
        System.out.println("线程调用 " + Thread.currentThread().getName());
        taskFactory.doSomething();
    }

    public void poolOne() {
        System.out.println("线程调用 " + Thread.currentThread().getName());
        taskFactory.poolOne();
    }

    public void poolTwo() {
        System.out.println("线程调用 " + Thread.currentThread().getName());
        taskFactory.poolTwo();
    }

}
