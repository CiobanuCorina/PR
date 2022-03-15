package com.company;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailSender {
    private String to;
    private String from;
    private String host;
    private String port;
    private String password;

    public EmailSender(String to, String from, String host, String port, String password) {
        this.to = to;
        this.from = from;
        this.host = host;
        this.port = port;
        this.password = password;
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
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Subject");
            message.setText("Testing message sending");
            message.setSentDate(new Date());
            System.out.println("sending...");
            SMTPTransport transport = (SMTPTransport) session.getTransport();
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Sent message successfully....");
            transport.close();
        } catch(MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
