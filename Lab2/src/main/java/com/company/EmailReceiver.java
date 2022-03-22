package com.company;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class EmailReceiver {
    private IMAPFolder folder;
    private IMAPStore store;
    private String host;
    private String port;
    User user;

    public EmailReceiver(String host, String port, User user) {
        this.host = host;
        this.port = port;
        this.user = user;
    }

    private Session getImapSession(){
        Properties properties = System.getProperties();
        properties.setProperty("mail.store.protocol","imaps");
        properties.setProperty("mail.imap.host",host);
        properties.setProperty("mail.imap.port", port);
        properties.put("mail.imap.ssl.enable", true);
        properties.put("mail.imap.starttls.enable", true);
        properties.put("mail.imap.ssl.trust", "*");
        properties.put("mail.imap.auth", true);
        return Session.getDefaultInstance(properties, null);
    }

    public Message[] getInbox() throws MessagingException {
        Session session = getImapSession();
        store = (IMAPStore) session.getStore("imaps");
        store.connect(host, user.getEmailAddress(), String.valueOf(user.getPassword()));
        folder = (IMAPFolder) store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        Message[] messages = folder.getMessages();
        System.out.println("Number of messages: " + folder.getMessageCount());
        System.out.println("Number of unread messages: " + folder.getUnreadMessageCount());
        return messages;
    }

    public void deleteMessagesFromInbox(Message[] messages, String subject) throws MessagingException {
        for( Message message : messages) {
            if(message.getSubject().equals(subject)) {
                message.setFlag(Flags.Flag.DELETED, true);
            }
        }
    }

    public Message searchForMessage(Message[] messages, String subject) throws MessagingException {
        for( Message message : messages) {
            if(message.getSubject().contains(subject)) {
                return message;
            }
        }
        return null;
    }

    public String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}
