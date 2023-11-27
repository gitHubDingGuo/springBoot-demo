package top.javahouse.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javahouse.mybatisplus.entity.User;
import top.javahouse.mybatisplus.mapper.UserMapper;
import top.javahouse.mybatisplus.service.UserService;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
