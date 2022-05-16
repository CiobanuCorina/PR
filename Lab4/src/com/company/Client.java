package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    public void execute() {
        try {
            client = new Socket("127.0.0.1", 1000);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler();
            Thread t = new Thread(inputHandler);
            t.start();

            String inMessage;

            while (!done && (inMessage = in.readLine()) != null) {
                System.out.println(inMessage);
            }

        } catch (IOException e) {
            System.out.println(e);
            try {
                shutdown();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void shutdown() throws IOException {
        done = true;
        in.close();
        out.close();
        if (!client.isClosed()) {
            client.close();
        }
    }


    public class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = inReader.readLine();
                    if (message.equals("exit")) {
                        out.println(message);
                        inReader.close();
                        shutdown();
                    } else {
                        out.println(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    shutdown();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        Client client = new Client();
        client.execute();
    }
}