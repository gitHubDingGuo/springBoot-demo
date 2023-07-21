package top.javahouse.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.swagger.dto.UserDto;
import top.javahouse.swagger.vo.UserVo;

/*
 * @author:javahouse.top
 * @Date: 2023/7/21 10:37
 */
@Api(tags = "Swagger接口")
@RestController
@RequestMapping("swagger")
public class SwaggerController {

    @ApiOperation(value = "获取one")
    @GetMapping("one")
    public ResponseEntity<?> one(@RequestBody UserDto userDto){
        return new ResponseEntity<>("one", HttpStatus.OK);
    }

    @ApiOperation(value = "获取two")
    @GetMapping("two")
    public ResponseEntity<?> two(@RequestBody UserDto userDto){
        UserVo userVo=new UserVo();
        userVo.setAge(18);
        userVo.setUserName("小明");
        return new ResponseEntity<>(userVo, HttpStatus.OK);
    }

}
