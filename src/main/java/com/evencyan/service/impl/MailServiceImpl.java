package com.evencyan.service.impl;

import com.evencyan.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async("genericExecutor")
    public void sendActivationMail(String subject, String to, String username, String activationUrl) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            Context context = new Context();
            context.setVariable("activationUrl", activationUrl);
            context.setVariable("username", username);
            String content = templateEngine.process("activation", context);
            helper.setText(content, true);
            mailSender.send(message);
            log.info("activation mail send successfully, username:" + username);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async("genericExecutor")
    public void sendExceptionMail(String type, String to, String info) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(type);
            Context context = new Context();
            context.setVariable("exceptionType", type);
            context.setVariable("exceptionInfo", info);
            String content = templateEngine.process("exception", context);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
