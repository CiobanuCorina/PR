package com.company;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.SizeLimitExceededException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailSender {
    private String to;
    private String host;
    private String port;
    private String subject;
    private String content;
    private User user;
    private MimeBodyPart attachmentPart = new MimeBodyPart();
    private File attachmentFilePath;

    public EmailSender(User user, String to, String host, String port, String subject, String content) {
        this.to = to;
        this.host = host;
        this.port = port;
        this.subject = subject;
        this.content = content;
        this.user = user;
    }

    public EmailSender(User user, String to, String host, String port, String subject, String content, File attachmentFilePath) {
        this.to = to;
        this.host = host;
        this.port = port;
        this.subject = subject;
        this.content = content;
        this.user = user;
        this.attachmentFilePath = attachmentFilePath;
    }

    public void sendEmail() {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", false);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.auth", true);

        Session session = Session.getInstance(properties, null);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user.getEmailAddress()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            message.setSentDate(new Date());
            if(attachmentFilePath != null) {
                if((attachmentFilePath.length() / Math.pow(1024, 2)) <= 2) {
                    attachmentPart.attachFile(attachmentFilePath);
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setText(content);
                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(attachmentPart);
                    multipart.addBodyPart(messageBodyPart);
                    message.setContent(multipart);
                }
                else throw new SizeLimitExceededException("Attachment file exceeds 2 MB");
            }
            System.out.println("sending...");
            SMTPTransport transport = (SMTPTransport) session.getTransport();
            transport.connect(host, user.getEmailAddress(), String.valueOf(user.getPassword()));
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Sent message successfully....");
            transport.close();
        } catch(MessagingException | IOException | SizeLimitExceededException mex) {
            mex.printStackTrace();
        }
    }
}
