package net.kenevans.apiservice;

import android.util.Log;

import androidx.annotation.NonNull;

import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.baseapi.definitions.AccessTokenService;
import net.kenevans.baseapi.definitions.RefreshTokenService;
import net.kenevans.baseapi.dto.AccessTokenOutput;
import net.kenevans.baseapi.dto.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAccessToken {
    public static void run(String refreshToken) {
        RetrofitInstanceWithBaseUrl<AccessTokenService> instance = new RetrofitInstanceWithBaseUrl<>(AccessTokenService.class);

//        AccessTokenService.AccessTokenBody body = new AccessTokenService.AccessTokenBody("refresh_token", refreshToken, "pRMT", "saturday$SHARE$scale");

        instance.getApiService().retrieveAccessTokenFromRefreshToken("refresh_token", refreshToken, "pRMT", "saturday$SHARE$scale").enqueue(new Callback<AccessTokenOutput>() {
            @Override
            public void onResponse(@NonNull Call<AccessTokenOutput> call, @NonNull Response<AccessTokenOutput> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String accessToken = response.body().getAccess_token();
                        //Store accessToken somewhere
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessTokenOutput> call, @NonNull Throwable t) {

            }
        });
    }
}
