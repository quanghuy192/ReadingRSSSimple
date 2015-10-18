package com.example.huydq17.readingrsssimple;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private List<Item> rssList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //abc
        startDownloadRss();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS: {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Downloading Data ....");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                return progressDialog;
            }
        }
        return null;
    }

    public void startDownloadRss() {
        new DownloadingRSSAsyntask().execute("http://rss.msn.com/vi-vn/");
        //    new DownloadingRSSAsyntask().execute("http://dantri.com.vn/trangchu.rss");
        //    new DownloadingRSSAsyntask().execute("https://cdn1.iconfinder.com/data/icons/nuove/128x128/apps/eclipse.png");

    }

    class DownloadingRSSAsyntask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(MainActivity.DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... params) {

            int count;
            try {
                URL url = new URL(params[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();

                int lenght = urlConnection.getContentLength();
                File rssFile = new File(Environment.getExternalStorageDirectory(),
                        "information.rss");
                if (rssFile.exists()) {
                    rssFile.delete();
                }
                rssFile.createNewFile();


                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                OutputStream out = new FileOutputStream(rssFile);

                byte[] buffer = new byte[1024];
                int total = 0;
                while ((count = in.read(buffer)) != -1) {
                    total += count;
                    int percent = (int) ((total * 100) / lenght);
                    publishProgress(percent);
                    out.write(buffer, 0, count);
                }
                out.flush();
                out.close();
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startActivity(new Intent(getApplicationContext(), ListFromRSS.class));
            progressDialog.dismiss();
        }
    }

}
