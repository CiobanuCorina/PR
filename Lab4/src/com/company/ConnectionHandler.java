package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private static int userCounter = 0;
    private int userNumber;
    private String userPrefix = "user";
    private Server server;

    public ConnectionHandler(Socket client, Server server) {
        this.client = client;
        this.server = server;
        userCounter++;
        this.userNumber = userCounter;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            sendMessage("Welcome to chat app! Enter exit to quit app");
            System.out.println(userPrefix + this.userNumber + " has connected");
            server.broadcast(userPrefix + this.userNumber + " entered the chat");

            String message;

            while ((message = in.readLine()) != null) {
                if (message.equals("exit")) {
                    server.broadcast(userPrefix + this.userNumber + " left the chat");
                    shutdown();
                    break;
                } else {
                    server.broadcast(userPrefix + this.userNumber + ": " + message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void shutdown() throws IOException {
        in.close();
        out.close();
        if (!client.isClosed()) {
            client.close();
        }
    }
}
