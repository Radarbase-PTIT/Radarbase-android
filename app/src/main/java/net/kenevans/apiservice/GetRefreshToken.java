package net.kenevans.apiservice;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.baseapi.definitions.RefreshTokenService;
import net.kenevans.baseapi.dto.Auth;
import net.kenevans.polar.polarecg.ECGActivity;
import net.kenevans.utils.ActivityHandling;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRefreshToken {
    public static void run(Context ctx, String tokenName) {
        RetrofitInstanceWithBaseUrl<RefreshTokenService> instance = new RetrofitInstanceWithBaseUrl<>(RefreshTokenService.class);

        instance.getApiService().getRefreshTokenFromUrl(tokenName).enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(@NonNull Call<Auth> call, @NonNull Response<Auth> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String refreshToken = response.body().getRefreshToken();
                    //Call to get access token
                    GetAccessToken.run(ctx, refreshToken);
                }

                //QR code is used
                else if (response.code() == 410) {
                    Toast.makeText(ctx, "QRCode used. Please rescan QRCode from management portal", Toast.LENGTH_LONG).show();
                    ActivityHandling activityHandling = new ActivityHandling();
                    activityHandling.switchActivityWithDelay(ctx, ECGActivity.class, 1000);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Auth> call, @NonNull Throwable t) {
                Log.e("ERROR", "Check network connection");
            }
        });
    }
}
