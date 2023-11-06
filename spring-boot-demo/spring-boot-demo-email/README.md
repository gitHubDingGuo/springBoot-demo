# spring-boot-demo-email

> 此 demo 主要演示了 Spring Boot 如何使用 email

## EmailController.java
```java
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
```
## MyMailService.java
```java
package top.javahouse.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/11/6 14:28
 */
@Component
public class MyMailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendMail(String from, String to, String cc, String subject, String text) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(from);//发送者
        smm.setBcc(cc);//抄送人
        smm.setTo(to);//收件人
        smm.setSubject(subject);//邮件主题
        smm.setText(text);//邮件内容
        javaMailSender.send(smm);//发送邮件
    }


    public void sendFileMail(String from, String to, String subject, String text, String filePath) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            //第2个参数:是否允许添加多部件
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            File file = new File(filePath);
            helper.addAttachment(file.getName(), file);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


}
```
## SpringBootDemoEmailApplication.java
```java
package top.javahouse.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @author:javahouse.top
 * @Description:
 * @Date: 2023/9/7 10:46
 */
@SpringBootApplication
public class SpringBootDemoEmailApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootDemoEmailApplication.class);
        application.run(args);
    }
}
```
## application.yml
```yml
server:
  port: 8000

spring:
  application:
    name: docker
  mail:
    host: smtp.qq.com #邮件服务器地址
    port: 465 #端口号
    username: 13232@qq.com #发送邮件账号
    password: fdsa!123 #授权码
    default-encoding: UTF-8 #默认编码格式
    properties:
      mail:
        debug: true #启动debug调试
        smtp:
          socketFactory:
            class: javax.net.ssl.SSLSocketFactory #SSL连接配置
```
