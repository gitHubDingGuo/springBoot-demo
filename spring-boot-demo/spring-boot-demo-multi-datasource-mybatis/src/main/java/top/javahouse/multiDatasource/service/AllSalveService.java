package top.javahouse.multiDatasource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.javahouse.multiDatasource.entity.User;

public interface AllSalveService extends IService<User> {

    void addSlaveUser(User user);
}
