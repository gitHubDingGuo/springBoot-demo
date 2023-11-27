package top.javahouse.multiDatasource.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahouse.multiDatasource.entity.User;
import top.javahouse.multiDatasource.hikariDatasource.annotation.HikariDataSource;
import top.javahouse.multiDatasource.hikariDatasource.config.HikariDatasourceConfig;
import top.javahouse.multiDatasource.mapper.UserMapper;
import top.javahouse.multiDatasource.druidDatasource.annotation.DataSource;
import top.javahouse.multiDatasource.druidDatasource.config.DynamicDataSourceConfig;
import top.javahouse.multiDatasource.service.UserService;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/11/7 17:01
 */
@Service
//@DS("master")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
/*
    @DS 可以注解在方法上或类上，同时存在就近原则 方法上注解 优先于 类上注解。
    注解	结果
    没有@DS	默认数据源
    @DS("dsName")	dsName可以为组名也可以为具体某个库的名称*/

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addMasterUser(User user) {
        userMapper.insert(user);
    }

    @DS("slave")
    @Override
    public void addSlaveUser(User user) {
        userMapper.insert(user);
    }

    @Override
    @DataSource(name = DynamicDataSourceConfig.DataSourceNames.DS_1)
    public void db1(User user) {
        userMapper.insert(user);
    }

    @Override
    @DataSource(name = DynamicDataSourceConfig.DataSourceNames.DS_2)
    public void db2(User user) {
        userMapper.insert(user);
    }

    @Override
    @HikariDataSource(name = HikariDatasourceConfig.DataSourceNames.HIKARI_ORDER)
    public void hikariDb2(User user) {
        userMapper.insert(user);
    }

    @Override
    @HikariDataSource(name = HikariDatasourceConfig.DataSourceNames.HIKARI_USER)
    public void hikariDb1(User user) {
        userMapper.insert(user);
    }
}
