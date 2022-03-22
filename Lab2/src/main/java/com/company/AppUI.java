package com.company;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;


public class AppUI {
    private JFrame frame;
    private JPanel panel;
    private User user;
    private EmailReceiver emailReceiver;
    File fileAttachment = null;

    public AppUI() {
        frame = new JFrame("Simple email app");
        frame.setPreferredSize(new Dimension(350, 250));
        panel = new JPanel();
        panel.setLayout(null);

        JLabel email = new JLabel("Email:");
        email.setBounds(10, 20, 100, 25);
        panel.add(email);

        JTextField inputEmail = new JTextField();
        inputEmail.setBounds(100, 20, 165, 25);
        panel.add(inputEmail);

        JLabel password = new JLabel("Password:");
        password.setBounds(10, 60, 80, 25);
        panel.add(password);

        JPasswordField inputPassword = new JPasswordField();
        inputPassword.setBounds(100, 60, 165, 25);
        panel.add(inputPassword);

        JButton login = new JButton("Login");
        login.setBounds(50, 100, 80, 25);
        panel.add(login);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        login.addActionListener(e -> {
            user = new User(inputEmail.getText(), inputPassword.getPassword());
            try {
                displayInboxPage();
            } catch (MessagingException | IOException ex) {
                ex.printStackTrace();
            }
            frame.dispose();
        });
        frame.setVisible(true);

    }

    public void displayInboxPage() throws MessagingException, IOException {
        JFrame inboxFrame = new JFrame("Inbox");
        inboxFrame.setPreferredSize(new Dimension(650, 550));
        JPanel inboxPanel = new JPanel();
        inboxPanel.setLayout(new BorderLayout());

        JLabel userEmail = new JLabel("Welcome " + user.getEmailAddress());
        userEmail.setBorder(BorderFactory.createEmptyBorder(5, 250, 20, 0));
        inboxPanel.add(userEmail, BorderLayout.PAGE_START);

        JButton composeEmailButton = new JButton("Compose new mail");
        composeEmailButton.setPreferredSize(new Dimension(200, 10));
        composeEmailButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inboxFrame.getBackground(), 10),
                BorderFactory.createEmptyBorder(5, 5, 10, 10)));
        inboxPanel.add(composeEmailButton, BorderLayout.WEST);

        JButton deleteEmails = new JButton("Delete");
        deleteEmails.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inboxFrame.getBackground(), 10),
                BorderFactory.createEmptyBorder(5, 5, 10, 10)));
        inboxPanel.add(deleteEmails, BorderLayout.AFTER_LINE_ENDS);

        String[] columnNames = {"Action", "From", "Subject", "Truncated Content"};
        emailReceiver = new EmailReceiver("imap.gmail.com", "993", user);
        Message[] messages = emailReceiver.getInbox();
        Object[][] obj = new Object[messages.length][];
        int index = 0;
        for(Message message : messages) {
            obj[index] = new Object[]{false, Arrays.toString(message.getFrom()), message.getSubject(), emailReceiver.getTextFromMessage(message)};
            index++;
        }
        DefaultTableModel inboxTableModel = new DefaultTableModel(obj, columnNames);
        JTable table = new JTable(inboxTableModel) {
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        JScrollPane scroll = new JScrollPane(table);
        table.setTableHeader(null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        inboxPanel.add(scroll, BorderLayout.AFTER_LAST_LINE);

        table.setPreferredSize(new Dimension(630, 400));

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                try {
                    if(table.getSelectedColumn() == 0) {
                        return;
                    }
                    Message message = emailReceiver.searchForMessage(messages, table.getValueAt(table.getSelectedRow(), 2).toString());
                    if(message != null) {
                        displayEmail(message);
                    }
                    else throw new NoSuchElementException("There is no such email");
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteEmails.addActionListener(e -> {
            for (int i = 0; i < table.getRowCount(); i++) {
                Boolean isChecked = Boolean.valueOf(table.getValueAt(i, 0).toString());

                if (isChecked) {
                    try {
                        emailReceiver.deleteMessagesFromInbox(messages, (String) table.getValueAt(i, 2));
                        inboxFrame.dispose();
                        displayInboxPage();
                    } catch (MessagingException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        inboxFrame.add(inboxPanel, BorderLayout.CENTER);
        inboxFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inboxFrame.pack();
        inboxFrame.setVisible(true);
        inboxFrame.setLocationRelativeTo(null);
        composeEmailButton.addActionListener(e -> displayEmailPage());
    }

    public void displayEmailPage() {
        EventQueue.invokeLater(() -> {
            JFrame emailCreateFrame = new JFrame("New email");
            emailCreateFrame.setPreferredSize(new Dimension(650, 600));
            JPanel emailCreatePanel = new JPanel();
            emailCreatePanel.setLayout(null);

            JButton returnBack = new JButton("Back");
            returnBack.setBounds(10, 20, 80, 25);
            emailCreatePanel.add(returnBack);
            returnBack.addActionListener(e -> emailCreateFrame.dispose());

            JLabel to = new JLabel("To:");
            to.setBounds(10, 60, 80, 25);
            emailCreatePanel.add(to);

            JTextField inputTo = new JTextField();
            inputTo.setBounds(100, 60, 165, 25);
            emailCreatePanel.add(inputTo);

            JLabel subject = new JLabel("Subject:");
            subject.setBounds(10, 100, 80, 25);
            emailCreatePanel.add(subject);

            JTextField inputSubject = new JTextField();
            inputSubject.setBounds(100, 100, 165, 25);
            emailCreatePanel.add(inputSubject);

            JLabel content = new JLabel("Content");
            content.setBounds(10, 140, 80, 25);
            emailCreatePanel.add(content);

            JTextArea inputContent = new JTextArea();
            inputContent.setBounds(100, 140, 300, 300);
            emailCreatePanel.add(inputContent);

            JButton chooseFile = new JButton("Choose file");
            chooseFile.setBounds(100, 450, 100, 30);
            JLabel filePath = new JLabel();
            filePath.setBounds(210, 445, 500, 50);
            emailCreatePanel.add(filePath);

            chooseFile.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose Your File");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
                {
                    fileAttachment = fileChooser.getSelectedFile();
                }
                if(fileAttachment != null) {
                    filePath.setText(fileAttachment.getPath());
                }
            });
            emailCreatePanel.add(chooseFile);

            JButton submit = new JButton("Submit");
            submit.setBounds(90, 490, 80, 25);
            emailCreatePanel.add(submit);
            submit.addActionListener(e -> {
                EmailSender emailSender;
                if(fileAttachment != null) {
                    emailSender = new EmailSender(
                            user,
                            inputTo.getText(),
                            "smtp.gmail.com",
                            "587",
                            inputSubject.getText(),
                            inputContent.getText(),
                            fileAttachment
                    );
                }
                else {
                    emailSender = new EmailSender(
                            user,
                            inputTo.getText(),
                            "smtp.gmail.com",
                            "587",
                            inputSubject.getText(),
                            inputContent.getText()
                    );
                }
                emailSender.sendEmail();
                emailCreateFrame.dispose();
            });


            emailCreateFrame.add(emailCreatePanel, BorderLayout.CENTER);
            emailCreateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            emailCreateFrame.pack();
            emailCreateFrame.setLocationRelativeTo(null);
            emailCreateFrame.setVisible(true);
        });
    }

    public void displayEmail(Message message) throws MessagingException, IOException {
        JFrame emailFrame = new JFrame(message.getSubject());
        emailFrame.setPreferredSize(new Dimension(650, 550));
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(null);

        JButton returnBack = new JButton("Back");
        returnBack.setBounds(10, 20, 80, 25);
        emailPanel.add(returnBack);
        returnBack.addActionListener(e -> emailFrame.dispose());

        JLabel from = new JLabel("From:");
        from.setBounds(10, 60, 80, 25);
        emailPanel.add(from);

        JLabel fromContent = new JLabel(Arrays.toString(message.getFrom()));
        fromContent.setBounds(120, 60, 500, 25);
        emailPanel.add(fromContent);

        JLabel content = new JLabel("Content:");
        content.setBounds(10, 100, 80, 25);
        emailPanel.add(content);

        JTextArea contentBody = new JTextArea(emailReceiver.getTextFromMessage(message));
        contentBody.setBounds(100, 100, 500, 400);
        contentBody.setWrapStyleWord(true);
        contentBody.setLineWrap(true);
        contentBody.setEditable(false);
        emailPanel.add(contentBody);

        emailFrame.add(emailPanel, BorderLayout.CENTER);
        emailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        emailFrame.pack();
        emailFrame.setLocationRelativeTo(null);
        emailFrame.setVisible(true);

    }
}
