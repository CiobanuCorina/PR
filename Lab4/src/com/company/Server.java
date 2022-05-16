package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ArrayList<ConnectionHandler> connections = new ArrayList<>();
    private ServerSocket server;
    private boolean done = false;
    private ExecutorService pool;

    public void execute() {
        try {
            server = new ServerSocket(1000);
            pool = Executors.newCachedThreadPool();

            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client, this);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                shutdown();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler ch : connections) {
            if (ch != null) {
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown() throws IOException {
        done = true;
        pool.shutdown();
        if (!server.isClosed()) {
            server.close();
        }
        for (ConnectionHandler ch : connections) {
            ch.shutdown();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.execute();
    }
}
