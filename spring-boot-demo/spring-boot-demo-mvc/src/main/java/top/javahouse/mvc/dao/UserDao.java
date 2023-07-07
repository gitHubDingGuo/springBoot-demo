package top.javahouse.mvc.dao;

import org.springframework.stereotype.Repository;
import top.javahouse.mvc.entity.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    public List<User> userList() {
        List<User> list=new ArrayList<>();
        User user=new User();
        user.setId(1);
        user.setName("www.javahouse.top");
        list.add(user);
        return list;
    }
}
