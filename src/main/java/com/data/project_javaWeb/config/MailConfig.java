package com.data.project_javaWeb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587); // Cổng TLS

        mailSender.setUsername("vubinh2005mt@gmail.com"); // Thay email
        mailSender.setPassword("ytxf yids hdfs saim");    // App Password (không phải mật khẩu Gmail)

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Debug mail
        props.put("mail.mime.charset", "UTF-8");
        
        return mailSender;
    }
}
