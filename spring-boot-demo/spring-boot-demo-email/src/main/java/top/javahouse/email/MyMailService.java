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
