package com.hamait.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

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



}
