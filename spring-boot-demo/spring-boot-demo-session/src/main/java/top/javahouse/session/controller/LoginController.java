package top.javahouse.session.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "登录")
public class LoginController {

    @ApiOperation("登录")
    @GetMapping("login")
    public ResponseEntity<?> login(HttpServletRequest request){
        System.out.println("contro-session="+request.getSession().getId());
        return ResponseEntity.ok(request.getSession().getId());
    }

}
