package com.oneworld.accuracy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async("threadPoolTaskExecutor")
    public void sendEmail(String filename, StringBuffer attachmentContent, String content, String subject, String to, String... cc) {
        //mailSender.send(email);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = null;
            if(attachmentContent != null) {
                messageHelper = new MimeMessageHelper(mimeMessage,true);
            }else{
                messageHelper = new MimeMessageHelper(mimeMessage,false);
            }

            messageHelper.setFrom("noreply@dml.com");
            messageHelper.setTo(to);
            if(cc != null) {
                messageHelper.setCc(cc);
            }
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            if(attachmentContent != null) {
                messageHelper.addAttachment(filename, new ByteArrayResource(attachmentContent.toString().getBytes(StandardCharsets.UTF_8)));
            }
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

   /* @Async("threadPoolTaskExecutor")
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }*/
}
