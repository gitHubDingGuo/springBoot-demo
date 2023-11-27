package top.javahouse.multiDatasource.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.multiDatasource.entity.User;
import top.javahouse.multiDatasource.mapper.UserMapper;
import top.javahouse.multiDatasource.service.AllSalveService;
import top.javahouse.multiDatasource.service.UserService;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AllSalveService allSalveService;

    @GetMapping("addMasterUser")
    public void addMasterUser() {
        User user =new User();
        user.setName("master-"+ UUID.randomUUID());
        user.setAge(18);
        userService.addMasterUser(user);
    }

    @GetMapping("addSlaveUser")
    public void addSlaveUser() {
        User user =new User();
        user.setName("slave-"+ UUID.randomUUID());
        user.setAge(18);
        userService.addSlaveUser(user);
    }

    @GetMapping("addAllSlaveUser")
    public void addAllSlaveUser() {
        User user =new User();
        user.setName("all-slave-"+ UUID.randomUUID());
        user.setAge(18);
        allSalveService.addSlaveUser(user);
    }

    @GetMapping("durid/db1")
    public void db1() {
        User user =new User();
        user.setName("db1-"+ UUID.randomUUID());
        user.setAge(18);
        userService.db1(user);
    }


    @GetMapping("durid/db2")
    public void db2() {
        User user =new User();
        user.setName("db2-"+ UUID.randomUUID());
        user.setAge(18);
        userService.db2(user);
    }

    @GetMapping("hikaridb1")
    public void hikariDb1() {
        User user =new User();
        user.setName("hikariDb1-"+ UUID.randomUUID());
        user.setAge(18);
        userService.hikariDb1(user);
    }

    @GetMapping("hikaridb2")
    public void hikariDb2() {
        User user =new User();
        user.setName("hikariDb2-"+ UUID.randomUUID());
        user.setAge(18);
        userService.hikariDb2(user);
    }


}
