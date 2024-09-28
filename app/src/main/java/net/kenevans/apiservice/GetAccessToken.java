package net.kenevans.apiservice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.apiservice.definitions.AccessTokenService;
import net.kenevans.apiservice.dto.AccessTokenOutput;
import net.kenevans.polar.polarecg.ECGActivity;
import net.kenevans.utils.Configurations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAccessToken {
    public static void run(Context ctx, String refreshToken) {
        RetrofitInstanceWithBaseUrl<AccessTokenService> instance = new RetrofitInstanceWithBaseUrl<>(AccessTokenService.class);

        instance.getApiService().retrieveAccessTokenFromRefreshToken("refresh_token", refreshToken, "pRMT", "saturday$SHARE$scale")
                .enqueue(new Callback<AccessTokenOutput>() {
                    @Override
                    public void onResponse(@NonNull Call<AccessTokenOutput> call, @NonNull Response<AccessTokenOutput> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String accessToken = response.body().getAccess_token();
                            //Decode accessToken to get project ID, sourceID, Patient Name
                            DecodedJWT decodedJWT = JWT.decode(accessToken);
                            String loginName = decodedJWT.getClaim("user_name").asString();
                            String[] sources = decodedJWT.getClaim("sources").asArray(String.class);
                            String source = sources[0];
                            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                            String project = roles[0].split(":")[0];

                            //Store configuration
                            Configurations.updatePreference(ctx, Configurations.SOURCE_ID, source);
                            Configurations.updatePreference(ctx, Configurations.PROJECT_ID, project);
                            Configurations.updatePreference(ctx, Configurations.PATIENT_NAME, loginName);
                            Configurations.updatePreference(ctx, Configurations.ACCESS_TOKEN, accessToken);

                            Toast.makeText(ctx, "Information stored", Toast.LENGTH_LONG).show();
                            //switch to configuration page to check data
                            Intent intent = new Intent(ctx, ECGActivity.class);
                            ctx.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AccessTokenOutput> call, @NonNull Throwable t) {
                        Log.e("ERROR", "Check network connection");
                    }
                });
    }
}
