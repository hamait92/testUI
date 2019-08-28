package com.hamait.test;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetAsyncTaskPost extends AsyncTask<String, Void, String> {

    OnGetResultsListener listener;
    private Context context;
    ProgressDialog progressDialog;
    int request_code;
    boolean check = true;
    JSONObject object;
    StringBuilder stringBuilder;

    public GetAsyncTaskPost(Context context, int request_code, JSONObject object) {
        this.context = context;
        this.request_code = request_code;

        this.object = object;


    }

    public void onGetResultsData(OnGetResultsListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        if (request_code < 1000 || request_code == 2000) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... params) {
        StringBuffer buffer = new StringBuffer();
        try {

            URL url = new URL(params[0]);
            //Log.e("URL:", "("+url);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setConnectTimeout(5 * 1000);
            httpUrlConn.setReadTimeout(5 * 1000);
            httpUrlConn.setRequestProperty("Content-Type", "application/json");
            httpUrlConn.setRequestProperty("Accept", "application/json");

            httpUrlConn.setRequestMethod("POST");
            //String lang = Constants.LANGUAGE;
         //   object.put("lang", lang);

            OutputStreamWriter wr = new OutputStreamWriter(httpUrlConn.getOutputStream());
            wr.write(object.toString());
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = httpUrlConn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpUrlConn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                Log.e("httpUrlConn:",""+ sb.toString());
                return  sb.toString();
            } else {
                return  httpUrlConn.getResponseMessage();

            }



        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected void onPostExecute(String response) {
        if (request_code < 1000 || request_code == 2000)
            progressDialog.dismiss();
        if (response != null && response.length() > 0) {
            listener.onGetResultsData(response, request_code);
        } else {
            Toast.makeText(context, "Data format error!", Toast.LENGTH_SHORT).show();
            listener.onGetResultsData(response, request_code);
        }
    }

    public interface OnGetResultsListener {
        public void onGetResultsData(String response, int request_code);
    }
}
