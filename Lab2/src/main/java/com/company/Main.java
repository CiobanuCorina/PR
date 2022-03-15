package com.company;

public class Main {

    public static void main(String[] args) {
        EmailSender emailSender = new EmailSender("receiver@gmail.com",
                "sender@gmail.com",
                "smtp.gmail.com",
                "587",
                "");
        emailSender.sendEmail();
    }
}
