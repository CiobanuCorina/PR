package com.company;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Client {

    private AudioInputStream audioStream;
    private AudioFormat format;
    private static boolean status = true;
    private String ip;
    private int port;
    private static int sampleRate = 44000;
    private DataLine.Info dataLineInfo;
    private SourceDataLine sourceDataLine;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void execute() throws Exception
    {
        System.out.println("Client started at port:" + this.port);

        System.setProperty("java.net.preferIPv4Stack", "true");

        InetAddress group = InetAddress.getByName(ip);
        MulticastSocket mSocket = new MulticastSocket(this.port);
        mSocket.setReuseAddress(true);
        mSocket.joinGroup(group);

        byte[] receiveData = new byte[4096];

        format = new AudioFormat(sampleRate, 16, 2, true, false);
        dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(format);
        sourceDataLine.start();

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        ByteArrayInputStream byteStream = new ByteArrayInputStream(receivePacket.getData());

        while (status)
        {
            mSocket.receive(receivePacket);
            audioStream = new AudioInputStream(byteStream, format, receivePacket.getLength());
            toSpeaker(receivePacket.getData());
        }
    }

    public void toSpeaker(byte[] soundbytes) {
        try
        {
            sourceDataLine.write(soundbytes, 0, soundbytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        sourceDataLine.close();
    }
}