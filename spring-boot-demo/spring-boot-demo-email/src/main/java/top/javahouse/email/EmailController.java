package top.javahouse.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private MyMailService MyMailService;

    @GetMapping("send")
    public void  send(){
        //文本
        MyMailService.sendMail("111@qq.com","222@qq.com","333@qq.com","1111","1111");
        //文本+附件
        MyMailService.sendFileMail("111@qq.com","2222@qq.com","发送带附件的邮件","邮件发送成功啦!","D:\\log.txt");
    }

}
