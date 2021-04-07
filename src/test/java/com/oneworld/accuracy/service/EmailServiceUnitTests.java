package com.oneworld.accuracy.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EmailServiceUnitTests {
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailService emailService = new EmailService(javaMailSender);

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSendEmail() throws Exception {
        ArgumentCaptor<MimeMessagePreparator> preparatorArgumentCaptor = ArgumentCaptor.forClass(MimeMessagePreparator.class);
        doNothing().when(javaMailSender).send(preparatorArgumentCaptor.capture());

        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();

        // given
        String from = "from@dail.com";
        String[] to = {"to@first-email.com", "to@second-email.com"};
        String subject = "subject";
        String msg = "<html>msg</html>";

        // when
        emailService.sendMessage(from, to, subject, msg);
        preparatorArgumentCaptor.getValue().prepare(mimeMessage);

        // then
        assertEquals(from, mimeMessage.getFrom()[0].toString());
        assertEquals(to[0], mimeMessage.getAllRecipients()[0].toString());
        assertEquals(to[1], mimeMessage.getAllRecipients()[1].toString());
        assertEquals(subject, mimeMessage.getSubject());
        assertEquals(msg, mimeMessage.getContent().toString());
    }
}
