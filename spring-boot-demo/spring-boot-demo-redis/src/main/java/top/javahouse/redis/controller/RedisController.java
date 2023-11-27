package top.javahouse.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.redis.entity.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(tags = "Redis")
@RestController
public class RedisController {


    // 注意：这里@Autowired是报错的，因为@Autowired按照类名注入的
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @ApiOperation("Add")
    @PostMapping("add")
    public ResponseEntity<?> add() {
        List<User> list=new ArrayList<>();
        for(int i=0;i<10;i++){
            User one=new User();
            one.setId(i+"");
            one.setName(i+"name");
            list.add(one);
        }
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("AutomaticConfirmation_autoAckTask", "AutomaticConfirmation_autoAckTask", 5 * 60, TimeUnit.SECONDS);
        redisTemplate.opsForList().rightPushAll("userList",list);
        return ResponseEntity.ok((List<User>)redisTemplate.opsForList().leftPop("userList"));
        //return ResponseEntity.ok(redisTemplate.opsForList().range("userList",0,999999L));
    }


    @ApiOperation("Find")
    @GetMapping("find/{userId}")
    public ResponseEntity<User> edit(@PathVariable("userId") String userId) {
        return ResponseEntity.ok((User)redisTemplate.opsForValue().get(userId));
    }

}
