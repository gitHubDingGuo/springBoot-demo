package top.javahouse.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahouse.mvc.dao.UserDao;
import top.javahouse.mvc.entity.User;
import top.javahouse.mvc.service.UserService;

import java.util.List;

/*
 * @author:javahouse.top
 * @Description:业务逻辑层
 * @Date:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> userList() {
        return userDao.userList();
    }
}
