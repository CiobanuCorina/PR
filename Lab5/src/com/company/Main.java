package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Live audio");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JButton share = new JButton();
        JButton connect = new JButton();
        share.setText("Share live audio");
        connect.setText("Connect to live audio");
        panel.add(share);
        panel.add(connect);

        share.addActionListener(e -> {
            frame.dispose();
            connectionDataView(true);
        });

        connect.addActionListener(e -> {
            frame.dispose();
            connectionDataView(false);
        });
        frame.add(panel);
        frame.setSize(500, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void connectionDataView(boolean isSharing) {
        JFrame frame = new JFrame("Connection data");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 0));
        JPanel ipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel connectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel ipLabel = new JLabel("Enter ip:");
        JTextField ip = new JTextField();
        ip.setColumns(10);
        JLabel portLabel = new JLabel("Enter port:");
        JTextField port = new JTextField("1000");
        JButton connect;
        if(isSharing) {
            connect = new JButton("Share");
            connect.addActionListener(e -> {
                startServerThread(frame, new Server(ip.getText(), Integer.parseInt(port.getText())));
            });
        } else {
            connect = new JButton("Connect");
            connect.addActionListener(e -> {
                startClientThread(frame, new Client(ip.getText(), Integer.parseInt(port.getText())));
            });
        }
        port.setColumns(10);
        ipPanel.add(ipLabel);
        ipPanel.add(ip);
        panel.add(ipPanel);
        portPanel.add(portLabel);
        portPanel.add(port);
        panel.add(portPanel);
        connectPanel.add(connect);
        panel.add(connectPanel);
        frame.add(panel);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void startServerThread(JFrame frame, Server server)
    {
        SwingWorker sw1 = new SwingWorker() {
            @Override
            protected Object doInBackground()
            {
                try {
                    server.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override protected void done()
            {
                frame.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        server.shutdown();
                        frame.dispose();
                    }
                });
            }
        };
        sw1.execute();
    }

    private static void startClientThread(JFrame frame, Client client)
    {
        SwingWorker sw1 = new SwingWorker() {
            @Override
            protected Object doInBackground()
            {
                try {
                    client.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override protected void done()
            {
                frame.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        client.shutdown();
                        frame.dispose();
                    }
                });
            }
        };
        sw1.execute();
    }
}
