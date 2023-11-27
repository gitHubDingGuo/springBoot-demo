package top.javahouse.multiDatasource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.javahouse.multiDatasource.entity.User;


public interface UserService extends IService<User> {

    void addMasterUser(User user);

    void addSlaveUser(User user);

    void db1(User user);

    void db2(User user);

    void hikariDb2(User user);

    void hikariDb1(User user);
}
