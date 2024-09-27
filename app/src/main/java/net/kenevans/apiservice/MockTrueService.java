package net.kenevans.apiservice;

import android.content.Context;
import android.util.Log;

import com.polar.sdk.api.model.PolarEcgData;

import net.kenevans.baseapi.RetrofitMockInstanceWithBaseUrl;
import net.kenevans.baseapi.definitions.MockService;
import net.kenevans.polar.polarecg.ECGPlotter;
import net.kenevans.polar.polarecg.IQRSConstants;
import net.kenevans.utils.Configurations;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MockTrueService {
    public static void run(Context ctx, String topic, PolarEcgData ecgData, int heartRate, int measurementTimes, int measurementCurrentState, ECGPlotter ecgPlotter)
    {

        int keySchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_KEY));
        int valueSchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_VALUE));
        String projectId = Configurations.getPreference(ctx, Configurations.PROJECT_ID);
        String sourceId = Configurations.getPreference(ctx, Configurations.SOURCE_ID);
        String patientName = Configurations.getPreference(ctx, Configurations.PATIENT_NAME);
        String accessToken = Configurations.getPreference(ctx, Configurations.ACCESS_TOKEN);


        RetrofitMockInstanceWithBaseUrl<MockService> instance = new RetrofitMockInstanceWithBaseUrl<>(MockService.class);
        for (Integer values : ecgData.samples) {
            HashMap<String, String> body = new HashMap<>();
            body.put("title", "title");
            body.put("body", "body");
            body.put("userId", "" + values * IQRSConstants.MICRO_TO_MILLI_VOLT);

            instance.getApiService().mockTrue(body).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("HEHEERR", t.getMessage());
                }
            });
        }
    }
}
