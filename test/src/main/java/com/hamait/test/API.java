package com.hamait.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class API {

    public static String LANGUAGE  ;

    public static void setLocale(String lang , Context context){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    public static void  loadLocale(Context context){
        SharedPreferences pref = context.getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = pref.getString("lang", "ar");
        LANGUAGE = lang;
        setLocale(lang,context);
    }

    public static void writeString(Context context, final String KEY, String property) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY, property); // Storing string
        editor.commit();

    }

    public static String readString(Context context, final String KEY) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        return pref.getString(KEY, null);
    }

    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            final Gson gson = new Gson();
            String serializedObject = gson.toJson(object);
            sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
            sharedPreferencesEditor.apply();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("saveObject",""+e);
        }
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
            if (sharedPreferences.contains(preferenceKey)) {
                final Gson gson = new Gson();
                return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static void cleardata(Context context){
        try {
            PreferenceManager.getDefaultSharedPreferences(context).
                    edit().clear().apply();
        }catch (Exception e){e.printStackTrace();}
    }

    public static Boolean hasString(Context context, final String KEY) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        if (pref.contains(KEY)){
            return true ;
        }else {
            return false;
        }
    }

    public static void deletString(Context context) {
        SharedPreferences mySPrefs = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.clear();

    }


    public static String generateString(int targetStringLength ){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

    public static String getPhoneSerialNumber() {
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception exception) {
            serial = "serialnumber";
        }
        return serial;
    }

    public static String getMacEth(Context context) {
        String macAddress;
        try {
            macAddress = loadFileAsString("/sys/class/net/eth0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            macAddress ="null";

        }
        if (macAddress.isEmpty()){
            macAddress ="null";
        }
        return macAddress.replace(":", "");

    }

    public static String getMacWifi(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            return wInfo.getMacAddress();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String loadFileAsString(String filePath) throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }


    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        InputStream is = new URL(url).openStream();
        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject readJsonFromUrlPost(String url , JSONObject object) throws IOException, JSONException {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection httpUrlConn = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setConnectTimeout(5 * 1000);
            httpUrlConn.setReadTimeout(5 * 1000);
            httpUrlConn.setRequestProperty("Content-Type", "application/json");
            httpUrlConn.setRequestProperty("Accept", "application/json");
            httpUrlConn.setRequestMethod("POST");

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
                JSONObject json = new JSONObject(sb.toString());
                httpUrlConn.disconnect();
                return json;
                //   return  sb.toString();
            } else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
