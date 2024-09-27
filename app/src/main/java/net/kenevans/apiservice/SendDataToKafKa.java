package net.kenevans.apiservice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.polar.sdk.api.model.PolarEcgData;

import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.baseapi.definitions.SendToKafkaService;
import net.kenevans.baseapi.dto.KafkaDataResponse;
import net.kenevans.baseapi.requests.PolarH10ECG;
import net.kenevans.baseapi.requests.polarh10ecg.RecordData;
import net.kenevans.baseapi.requests.polarh10ecg.RecordKeyData;
import net.kenevans.baseapi.requests.polarh10ecg.RecordValueData;
import net.kenevans.polar.polarecg.ECGPlotter;
import net.kenevans.polar.polarecg.IQRSConstants;
import net.kenevans.utils.Configurations;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendDataToKafKa {
    public static void run(
            Context ctx,
            String topic,
            PolarEcgData polarEcgData,
            int heartRate,
            int measurementTimes,
            int measurementCurrentState,
            ECGPlotter ecgPlotter
    ) {
        // Plotting
        ecgPlotter.addValues(polarEcgData);

        // Prepare data
        RetrofitInstanceWithBaseUrl<SendToKafkaService> instance = new RetrofitInstanceWithBaseUrl<>(SendToKafkaService.class);
        int keySchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_KEY));
        int valueSchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_VALUE));
        String projectId = Configurations.getPreference(ctx, Configurations.PROJECT_ID);
        String sourceId = Configurations.getPreference(ctx, Configurations.SOURCE_ID);
        String patientName = Configurations.getPreference(ctx, Configurations.PATIENT_NAME);
        String accessToken = Configurations.getPreference(ctx, Configurations.ACCESS_TOKEN);

        //Sending to kafka
        for (Integer values : polarEcgData.samples) {
            RecordData[] recordData = new RecordData[] {
                    new RecordData(
                            new RecordKeyData(projectId, sourceId, patientName),
                            new RecordValueData(values * IQRSConstants.MICRO_TO_MILLI_VOLT, heartRate, measurementTimes, measurementCurrentState)
                    )
            };

            PolarH10ECG data = new PolarH10ECG(keySchemaId, valueSchemaId, recordData);

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);
            headers.put("Content-Type", "application/vnd.kafka.avro.v2+json");
            headers.put("Accept", "application/json");

            instance.getApiService().sendData(topic, headers, data).enqueue(new Callback<KafkaDataResponse>() {
                @Override
                public void onResponse(@NonNull Call<KafkaDataResponse> call, @NonNull Response<KafkaDataResponse> response) {
                    Log.d("Success", "Data sent");
                }

                @Override
                public void onFailure(@NonNull Call<KafkaDataResponse> call, @NonNull Throwable t) {
                    Log.e("Failure", "Cannot send request");
                }
            });
        }

    }
}
