package net.kenevans.apiservice;

import android.util.Log;

import androidx.annotation.NonNull;

import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.baseapi.definitions.RefreshTokenService;
import net.kenevans.baseapi.dto.Auth;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRefreshToken {
    public static void run(String tokenName) {
        RetrofitInstanceWithBaseUrl<RefreshTokenService> instance = new RetrofitInstanceWithBaseUrl<>(RefreshTokenService.class);

        instance.getApiService().getRefreshTokenFromUrl(tokenName).enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(@NonNull Call<Auth> call, @NonNull Response<Auth> response) {
                if (response.isSuccessful()) {
                    Log.d("INFO", response.body().getRefreshToken());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Auth> call, @NonNull Throwable t) {

            }
        });
    }
}
