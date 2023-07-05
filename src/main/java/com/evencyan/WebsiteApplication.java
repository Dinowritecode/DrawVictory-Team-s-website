package com.evencyan;

//import org.jline.utils.AttributedString;
//import org.jline.utils.AttributedStyle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.shell.jline.PromptProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Random;

/**
 * @author evencyan
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync(proxyTargetClass = true, mode = AdviceMode.ASPECTJ)
@EnableScheduling
@EnableAspectJAutoProxy
@MapperScan("com.evencyan.dao")
public class WebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsiteApplication.class, args);
    }

    @Bean
    public Random random() {
        return new Random();
    }

//    @Bean
//    public PromptProvider PromptProvider() {
//        return () -> new AttributedString(">", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
//    }
}
