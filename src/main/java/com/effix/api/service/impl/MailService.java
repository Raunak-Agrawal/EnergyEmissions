package com.effix.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Service
public class MailService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Async
    public void sendMail(String from, String to, String subject, String bodyPlainText, String bodyHtml) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(subject);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(bodyPlainText, "text/plain; charset=UTF-8");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(bodyHtml, "text/html; charset=UTF-8");

            Multipart multiPart = new MimeMultipart("alternative");

            // create a new imagePart and add it to multipart so that the image is inline attached in the email
//            Resource resource = new ClassPathResource("logo.png");
//            File file = resource.getFile();
//            byte[] fileContent = FileUtils.readFileToByteArray(file);
//            String encodedString = Base64.getEncoder().encodeToString(fileContent);
//            byte[] rawImage = Base64.getDecoder().decode(encodedString);
//            BodyPart imagePart = new MimeBodyPart();
//            ByteArrayDataSource imageDataSource = new ByteArrayDataSource(rawImage, "image/png");
//
//            imagePart.setDataHandler(new DataHandler(imageDataSource));
//            imagePart.setHeader("Content-ID", "<logoImage>");
//            imagePart.setFileName("logo.png");
//
//            multiPart.addBodyPart(imagePart);
            multiPart.addBodyPart(textPart);
            multiPart.addBodyPart(htmlPart);

            message.setContent(multiPart);

            javaMailSender.send(message);

            logger.info("Mail sent to: {}, with subject: {}", to, subject);
        } catch (MessagingException e) {
            logger.error(e.getMessage());
        }
    }
}
