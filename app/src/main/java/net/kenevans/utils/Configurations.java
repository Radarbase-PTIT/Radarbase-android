package net.kenevans.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Map;

public class Configurations {

    public static final String SCHEME = "scheme";
    public static final String HOST = "host";
    public static final String PROJECT_ID = "projectID";
    public static final String SOURCE_ID = "sourceID";
    public static final String PATIENT_NAME = "patientName";
    public static final String ANDROID_POLAR_H10_ECG_KEY = "android_polar_h10_ecg-key";
    public static final String ANDROID_POLAR_H10_ECG_VALUE = "android_polar_h10_ecg-value";
    public static final String ACCESS_TOKEN = "access_token";


    public static void updatePreference(Context ctx, String key, String value) {
        //Change settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Get all preferences
     * @param ctx
     * @return all preferences
     */
    public static Map<String, ?> getPreferences(Context ctx) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getAll();
    }

    public static String getPreference(Context ctx, String key) {
        Object o = getPreferences(ctx).get(key);
        return o == null ? null : o.toString();
    }


}
