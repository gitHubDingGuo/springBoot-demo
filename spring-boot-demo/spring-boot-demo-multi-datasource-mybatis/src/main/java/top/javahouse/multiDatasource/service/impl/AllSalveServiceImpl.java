package top.javahouse.multiDatasource.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahouse.multiDatasource.entity.User;
import top.javahouse.multiDatasource.mapper.UserMapper;
import top.javahouse.multiDatasource.service.AllSalveService;
import top.javahouse.multiDatasource.service.UserService;

/*
 * @author:javahouse.top
 * @Description: 类级别，全部插入从库
 * @Date: 2023/11/7 17:20
 */
@Service
@DS("slave")
public class AllSalveServiceImpl extends ServiceImpl<UserMapper, User> implements AllSalveService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public void addSlaveUser(User user) {
        userMapper.insert(user);
    }
}
