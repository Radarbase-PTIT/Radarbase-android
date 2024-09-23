package net.kenevans.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

public class ActivityHandling {
    public void switchActivityWithDelay(Context currentActivity, final Class<? extends Activity> targetActivity, int delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start ActivityB with an Intent
                Intent intent = new Intent(currentActivity, targetActivity);
                currentActivity.startActivity(intent);
            }
        }, delayMillis); // Delay for 3 seconds
    }
}
