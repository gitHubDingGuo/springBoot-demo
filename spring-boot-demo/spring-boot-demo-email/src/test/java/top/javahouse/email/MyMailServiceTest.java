package top.javahouse.email;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest

class MyMailServiceTest {

    @Autowired
    private MyMailService MyMailService;

    @Test
    void doSomething() {
        MyMailService.sendMail("992279357@qq.com","992279357@qq.com","992279357@qq.com","1111","1111");
    }

    @Test
    public void sendFile(){
        MyMailService.sendFileMail("srm@qdama.cn","992279357@qq.com","发送带附件的邮件","邮件发送成功啦!","D:\\log.txt");
    }

}