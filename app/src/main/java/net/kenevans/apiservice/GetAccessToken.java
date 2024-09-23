package net.kenevans.apiservice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.baseapi.definitions.AccessTokenService;
import net.kenevans.baseapi.dto.AccessTokenOutput;
import net.kenevans.polar.polarecg.SettingsActivity;
import net.kenevans.utils.ActivityHandling;
import net.kenevans.utils.Configurations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAccessToken {
    public static void run(Context ctx, String refreshToken) {
        RetrofitInstanceWithBaseUrl<AccessTokenService> instance = new RetrofitInstanceWithBaseUrl<>(AccessTokenService.class);

//        AccessTokenService.AccessTokenBody body = new AccessTokenService.AccessTokenBody("refresh_token", refreshToken, "pRMT", "saturday$SHARE$scale");

        instance.getApiService().retrieveAccessTokenFromRefreshToken("refresh_token", refreshToken, "pRMT", "saturday$SHARE$scale").enqueue(new Callback<AccessTokenOutput>() {
            @Override
            public void onResponse(@NonNull Call<AccessTokenOutput> call, @NonNull Response<AccessTokenOutput> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String accessToken = response.body().getAccess_token();
                        String refreshToken = response.body().getRefresh_token();
                        //Decode accessToken to get project ID, sourceID, Patient Name
                        DecodedJWT decodedJWT = JWT.decode(accessToken);
                        String loginName = decodedJWT.getClaim("user_name").asString();
                        String[] sources = decodedJWT.getClaim("sources").asArray(String.class);
                        String source = sources[0];
                        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                        String project  = roles[0].split(":")[0];

                        //Store configuration
                        Configurations.updatePreference(ctx, "sourceID", source);
                        Configurations.updatePreference(ctx, "projectID", project);
                        Configurations.updatePreference(ctx, "patientName", loginName);
                        Configurations.updatePreference(ctx, "accessToken", accessToken);

                        //switch to configuration page to check data
                        Intent intent = new Intent(ctx, SettingsActivity.class);
                        ctx.startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessTokenOutput> call, @NonNull Throwable t) {
                Log.e("ERROR", "Check network connection");
            }
        });
    }
}
