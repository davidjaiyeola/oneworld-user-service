package com.oneworld.accuracy.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

   @Async("threadPoolTaskExecutor")
   public void sendMessage(final String from, final String[] to, final String subject, final String msg) {
        try {
            mailSender.send(new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws MessagingException {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                    message.setFrom(from);
                    message.setTo(to);
                    message.setSubject(subject);
                    message.setText(msg, true);
                }
            });
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
