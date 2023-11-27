package top.javahouse.filter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterController {

    @GetMapping("cas/login")
    public Object queryCityByCityName() {
        return "执行成功";
    }

    @GetMapping("cas/other")
    public Object other() {
        return "执行成功";
    }

}
