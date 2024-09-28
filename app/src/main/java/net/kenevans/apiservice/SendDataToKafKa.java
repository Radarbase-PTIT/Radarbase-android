package net.kenevans.apiservice;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.polar.sdk.api.model.PolarEcgData;

import net.kenevans.apiservice.callbacks.IResponseCallback;
import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.apiservice.definitions.SendToKafkaService;
import net.kenevans.apiservice.dto.KafkaDataResponse;
import net.kenevans.apiservice.requests.PolarH10ECG;
import net.kenevans.apiservice.requests.polarh10ecg.RecordData;
import net.kenevans.apiservice.requests.polarh10ecg.RecordKeyData;
import net.kenevans.apiservice.requests.polarh10ecg.RecordValueData;
import net.kenevans.polar.polarecg.IQRSConstants;
import net.kenevans.utils.Configurations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendDataToKafKa {
    public static void run(
            Context ctx,
            String topic,
            PolarEcgData polarEcgData,
            List<Integer> heartRate,
            int measurementTimes,
            IResponseCallback responseCallback
    ) {
        responseCallback.onSuccess();

        // Prepare data
        RetrofitInstanceWithBaseUrl<SendToKafkaService> instance = new RetrofitInstanceWithBaseUrl<>(SendToKafkaService.class);
        int keySchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_KEY));
        int valueSchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_VALUE));
        String projectId = Configurations.getPreference(ctx, Configurations.PROJECT_ID);
        String sourceId = Configurations.getPreference(ctx, Configurations.SOURCE_ID);
        String patientName = Configurations.getPreference(ctx, Configurations.PATIENT_NAME);
        String accessToken = Configurations.getPreference(ctx, Configurations.ACCESS_TOKEN);

        //Sending to kafka
            RecordData[] recordData = new RecordData[] {
                    new RecordData(
                            new RecordKeyData(projectId, sourceId, patientName),
                            new RecordValueData(
                                    polarEcgData.samples.stream().map(item -> item * IQRSConstants.MICRO_TO_MILLI_VOLT).collect(Collectors.toList()),
                                    heartRate,
                                    measurementTimes
                            )
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
                    if (response.code() >=400) {
                        Toast.makeText(ctx, "Access token expired. Rescan QR code", Toast.LENGTH_LONG).show();
                        if (!call.isCanceled()) {
                            call.cancel();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<KafkaDataResponse> call, @NonNull Throwable t) {
                    Log.e("Failure", "Cannot send request");
                }
            });

    }
}
