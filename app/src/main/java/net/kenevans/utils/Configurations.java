package net.kenevans.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Map;
import java.util.Objects;

public class Configurations {
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
     * @param key
     * @return
     */
    public static Map<String, ?> getPreferences(Context ctx) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getAll();
    }

    public static String getPreference(Context ctx, String key) {
        return Objects.requireNonNull(getPreferences(ctx).get(key)).toString();
    }

}
