package com.company;

import java.io.IOException;
import java.net.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Server {
    private TargetDataLine line;
    private DatagramPacket datagram;
    private static final AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    private static final int rate = 44000;
    private static final int channels = 2;
    private static final int sampleSize = 16;
    private String ip;
    private int port;

    public Server(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void execute()
    {
        System.setProperty("java.net.preferIPv4Stack", "true");
        InetAddress addr;

        System.out.println("Server started at port:" + this.port);

        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, false);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " not supported.");
            return;
        }

        try
        {
            this.line = (TargetDataLine) AudioSystem.getLine(info);
            this.line.open(format);
            this.line.start();

            byte[] data = new byte[4096];

            addr = InetAddress.getByName(ip);
            MulticastSocket socket = new MulticastSocket();
            while (true) {
                this.line.read(data, 0, data.length);
                this.datagram = new DatagramPacket (data, data.length, addr, this.port);

                socket.send(this.datagram);
            }
        }catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        line.close();
    }
}
