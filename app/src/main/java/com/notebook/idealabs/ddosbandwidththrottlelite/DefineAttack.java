package com.notebook.idealabs.ddosbandwidththrottlelite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static com.notebook.idealabs.ddosbandwidththrottlelite.R.*;

public class DefineAttack extends Activity {
    EditText Attackurl;
    private String Ip_Address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_define_attack);
        Attackurl = findViewById(id.AttackOn);

        // Starting Shared Preferences
        final SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("Access", true)) {
            new AlertDialog.Builder(DefineAttack.this).setTitle("Warning!!").setMessage(getString(string.Alert)).setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    editor.putBoolean("Access", false);
                    editor.apply();
                }
            }).setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    editor.putBoolean("Access", true);
                    editor.apply();
                    DefineAttack.this.finish();
                    DefineAttack.this.moveTaskToBack(true);
                }
            }).setIcon(mipmap.ic_launcher).show();
        }
        if (sharedPreferences.getBoolean("Rate", true)) {
            editor.putInt("Count", sharedPreferences.getInt("Count", 0) + 1);
            editor.apply();
            if(sharedPreferences.getInt("Count", 0) == 5 || sharedPreferences.getInt("Count", 0) == 10 || (sharedPreferences.getInt("Count", 0)%20) == 0) {
                new AlertDialog.Builder(DefineAttack.this).setTitle("Enjoying the App").setMessage(getString(string.rate)).setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("Rate", false);
                        editor.apply();
                        try {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.notebook.idealabs.ddosbandwidththrottlelite")));
                            return;
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.notebook.idealabs.ddosbandwidththrottlelite")));
                            return;
                        }
                    }
                }).setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("Rate", true);
                    }
                }).setIcon(mipmap.ic_launcher).show();
            }
        }
        //End


    }


    public void LockTarget(View view) {
        if (internet()) {
            String urlLockOn = null;
            try {
                urlLockOn = Attackurl.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (urlLockOn == null || urlLockOn.equals("")) {
                Attackurl.setError("This Field Needs To Be filled");
                return;
            } else {
                this.Ip_Address = "";
                GetIp(urlLockOn);
                // Attempting to Move To Next Screen
                String check;

                Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                check = this.Ip_Address;
                if(check != null && check.compareToIgnoreCase("None") != 0 && check.compareToIgnoreCase("") != 0) {
                    Toast.makeText(this, this.Ip_Address, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AttackScreen.class);
                    intent.putExtra("Target", check);
                    startActivity(intent);
                    return;
                }
                else{
                    Toast.makeText(this, "Failed To Map Domain Name With Ip Address.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        Toast.makeText(this, "Internet is Required", Toast.LENGTH_SHORT).show();
    }

    public boolean internet() {
        boolean wifi = false;
        boolean phone = false;
        for (NetworkInfo networkInfo : ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
            if (networkInfo.getTypeName().equalsIgnoreCase("wifi") && networkInfo.isConnected()) {
                wifi = true;
            }
            if (networkInfo.getTypeName().equalsIgnoreCase("mobile") && networkInfo.isConnected()) {
                phone = true;
            }
        }
        return wifi || phone;
    }

    public void GetIp(final String address) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    String domain = address.replace("http://", "").replace("www.", "").replace(" ", "");
                    if (domain.length() > 0) {
                        while(DefineAttack.this.Ip_Address.equalsIgnoreCase(""))
                            DefineAttack.this.Ip_Address = InetAddress.getByName(domain).getHostAddress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.share_menu_id:
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Hey check out this new app at: https://play.google.com/store/apps/details?id=com.notebook.idealabs.ddosbandwidththrottlelite");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
                return true;
            case id.rate_us_menu_id:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.notebook.idealabs.ddosbandwidththrottlelite")));
                    return true;
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.notebook.idealabs.ddosbandwidththrottlelite")));
                    return true;
                }
            case id.more_apps_menu_id:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://developer?id=7665922803421240272")));
                    return true;
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/dev?id=7665922803421240272")));
                    return true;
                }
            case R.id.credits:
                Intent intent = new Intent(this, Translations.class);
                startActivity(intent);
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

    /*public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }*/
}
