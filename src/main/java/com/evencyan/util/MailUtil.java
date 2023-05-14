package com.evencyan.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
    private static JavaMailSender mailSender;
    private static String sender;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        MailUtil.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    public void setSender(String sender) {
        MailUtil.sender = sender;
    }

    public static void sendMail(String receiveMailAccount,
                                String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);// 设置发件人邮箱（若配置默认邮箱则不用再设置）
            helper.setTo(receiveMailAccount);// 设置收件人邮箱
            helper.setSubject(subject);// 设置邮件主题
            helper.setText(content,true);// 设置邮件文本内容
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static String mailBody = """
            <div style='position:absolute;'>
                <div style='position: relative;
                        margin: auto;
                        left: 0;
                        right: 0;
                        width: 630px;
                        height: 700px;
                        background-color: #24292d;'>
                    <img style='width: 100px;
                        position: absolute;
                        left: 0;
                        right: 0;
                        top: 50px;
                        margin: auto;' src='https://www.gaojianwen.xyz/picture/logo.svg'>
                    <h1 style=' width: 180px;
                        position: absolute;
                        left: 245px;
                        top: 210px;
                        margin: auto;
                        font-size: 35px;
                        font-weight: 600;
                        color: #ff7675;'>账户激活</h1>
                    <div style='width: 80%;
                        position: absolute;
                        left: 0;
                        right: 0;
                        top: 270px;
                        margin: auto;
                        height: 2px;
                        background-color: #ff7675;'></div>
                    <p style='color: #ff7675;
                        font-size: 20px;
                        position: absolute;
                        top: 280px;
                        left: 65px;'>亲爱的%username%:</p>
                    <p style='height: 40px;
                        width: 75%;
                        position: absolute;
                        left: 0;
                        right: 0;
                        margin: auto;
                        font-size: 17px;
                        color: white;
                        top: 345px;'>
                        感谢您在我们的网站注册了账号，为了确保账户唯一性以及您不是个机器人，需要您使用邮箱激活账户，激活链接如下，请点击链接并完成账户初始设置，谢谢合作</p>
                    <a style='position: absolute;
                        background-color: #24292d;
                        color: #ff7675;
                        border: 2px solid #ff7675;
                        padding: 6px 10px;
                        letter-spacing: 1px;
                        font-size: 26px;
                        outline: none;
                        height: 35px;
                        width: 163px;
                        text-decoration: none;
                        top: 450px;
                        right: 0;
                        left: 0;
                        margin: auto;
                        cursor: pointer;' target='_blank' href='%link%'>点我激活账户</a>
                    <p style='position: absolute;
                        font-size: 14px;
                        color: white;
                        width: 70%;
                        margin: auto;
                        left: 0;
                        right: 0;
                        bottom: 150px;'>无法跳转？请复制以下链接进行激活
                        <a target='_blank' href='%link%'>%link%</a>
                    </p>
                    <p style='position: absolute;
                        line-height: 0;
                        color: white;
                        font-weight: 500;
                        bottom: 90px;
                        left: 400px;'>祝您愉快！</p>
                    <p style='position: absolute;
                        line-height: 0;
                        color: white;
                        font-weight: 500;
                        bottom: 70px;
                        left: 400px;'>DrawVictory Team</p>
                    <div style='position: absolute;
                        height: 40px;
                        width: 3px;
                        background-color: #ff7675;
                        bottom: 77px;
                        right: 70px;'></div>
                </div>
            </div>
            """;
}