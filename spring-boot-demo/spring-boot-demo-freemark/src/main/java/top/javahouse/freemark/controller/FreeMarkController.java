package top.javahouse.freemark.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.javahouse.freemark.entity.Student;

@Controller //用于跳转页面，而非返回json数据
public class FreeMarkController {

    /**
     * 入门
     */
    @GetMapping("/user")
    public String freemark(Model model) {
        // 也可以使用HttpServletRequest中的setAttribute, Model底层也是封装了HttpServletRequest
        //存储数据
        model.addAttribute("name", "jack");
        Student student = new Student();
        student.setName("小明");
        model.addAttribute("student", student);
        //路径： prefix+hello+suffix
        //classpath:/templates/hello.ftl
        return "freemark";//返回展示数据的页面名称
    }

    @GetMapping("/listUser")
    public String listUser(Model model) {
        // 也可以使用HttpServletRequest中的setAttribute, Model底层也是封装了HttpServletRequest
        //存储数据
        model.addAttribute("name", "jack");
        Student student = new Student();
        student.setName("小明");
        model.addAttribute("student", student);
        //路径： prefix+hello+suffix
        //classpath:/templates/hello.ftl
        return "freemark";//返回展示数据的页面名称
    }

    @GetMapping("/date")
    public String date(Student student) {
        System.out.println(student);
        return "";
    }

}