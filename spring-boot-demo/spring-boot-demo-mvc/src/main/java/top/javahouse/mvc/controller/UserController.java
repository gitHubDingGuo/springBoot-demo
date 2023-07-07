package top.javahouse.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.mvc.entity.User;
import top.javahouse.mvc.service.UserService;

import java.util.List;

/*
 * @author:javahouse.top
 * @Description:表现层
 * @Date:  
 */
@RestController
@RequestMapping("/mvc")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("list")
    public ResponseEntity<List<User>> userList() {
        return new ResponseEntity<>(userService.userList(), HttpStatus.OK);
    }


}
