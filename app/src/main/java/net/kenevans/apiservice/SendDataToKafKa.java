package net.kenevans.apiservice;

import android.content.Context;
import android.util.Log;

import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.baseapi.definitions.SendToKafkaService;
import net.kenevans.baseapi.dto.KafkaDataResponse;
import net.kenevans.baseapi.requests.PolarH10ECG;
import net.kenevans.baseapi.requests.polarh10ecg.RecordData;
import net.kenevans.baseapi.requests.polarh10ecg.RecordKeyData;
import net.kenevans.baseapi.requests.polarh10ecg.RecordValueData;
import net.kenevans.utils.Configurations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendDataToKafKa {
    public static void run(Context ctx, String topic, double ecg) {
        RetrofitInstanceWithBaseUrl<SendToKafkaService> instance = new RetrofitInstanceWithBaseUrl<>(SendToKafkaService.class);

        int keySchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_KEY));
        int valueSchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_VALUE));
        String projectId = Configurations.getPreference(ctx, Configurations.PROJECT_ID);
        String sourceId = Configurations.getPreference(ctx, Configurations.SOURCE_ID);
        String patientName = Configurations.getPreference(ctx, Configurations.PATIENT_NAME);
        String accessToken = Configurations.getPreference(ctx, Configurations.ACCESS_TOKEN);

        RecordData[] recordData = new RecordData[] {
                new RecordData(
                        new RecordKeyData(projectId, sourceId, patientName),
                        new RecordValueData(ecg)
                )
        };

        PolarH10ECG data = new PolarH10ECG(keySchemaId, valueSchemaId, recordData);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Content-Type", "application/vnd.kafka.avro.v2+json");
        headers.put("Accept", "application/json");

        instance.getApiService().sendData(topic, headers, data).enqueue(new Callback<KafkaDataResponse>() {
            @Override
            public void onResponse(Call<KafkaDataResponse> call, Response<KafkaDataResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("AAAA", response.body().toString());
                }

                Log.e("ERROR", response.headers().toString());
            }

            @Override
            public void onFailure(Call<KafkaDataResponse> call, Throwable t) {
                Log.e("Failure", "Cannot send request");
            }
        });
    }
}
