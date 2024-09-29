package net.kenevans.apiservice;

import android.content.Context;
import android.util.Log;
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
import net.kenevans.polar.polarecg.ECGPlotter;
import net.kenevans.polar.polarecg.IQRSConstants;
import net.kenevans.utils.Configurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendDataToKafKa
{
    private int sendCount;
    private List<Integer> heartRates;
    private List<Double> ecgData;
    private final RetrofitInstanceWithBaseUrl<SendToKafkaService> instance;

    public SendDataToKafKa() {
        this.instance = new RetrofitInstanceWithBaseUrl<>(SendToKafkaService.class);
        this.heartRates = new ArrayList<>();
        this.ecgData = new ArrayList<>();
        this.sendCount = 0;
    }

    public void run(
            Context ctx,
            String topic,
            PolarEcgData polarEcgData,
            int measurementTimes,
            int heartRate,
            boolean playing,
            IResponseCallback responseCallback
    ) {

        //Update UI
        responseCallback.onSuccess();

        sendCount += polarEcgData.samples.size();

        for (Integer value : polarEcgData.samples) {
            //if recording, send samples per 5 seconds each = 73
            if (ecgData.size() == 5 * 73) {
                //send data
                send(ctx, topic, measurementTimes);
                this.ecgData = this.ecgData.subList(5 * 73, this.ecgData.size());
                this.heartRates = this.heartRates.subList(5 * 73, this.heartRates.size());
            } else {
                ecgData.add(value * IQRSConstants.MICRO_TO_MILLI_VOLT);
                heartRates.add(heartRate);
            }
        }

        if (!playing) {

        }
    }

    /**
     * Send data to kafka
     * @param ctx
     * @param measurementTimes
     */
    public void send(Context ctx, String topic, int measurementTimes) {
        int keySchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_KEY));
        int valueSchemaId = Integer.parseInt(Configurations.getPreference(ctx, Configurations.ANDROID_POLAR_H10_ECG_VALUE));
        String projectId = Configurations.getPreference(ctx, Configurations.PROJECT_ID);
        String sourceId = Configurations.getPreference(ctx, Configurations.SOURCE_ID);
        String patientName = Configurations.getPreference(ctx, Configurations.PATIENT_NAME);
        String accessToken = Configurations.getPreference(ctx, Configurations.ACCESS_TOKEN);

        RecordData[] recordData = new RecordData[] {
                new RecordData(
                        new RecordKeyData(projectId, sourceId, patientName),
                        new RecordValueData(this.ecgData,this.heartRates,measurementTimes)
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
                if (response.code() >= 400) {
                    Toast.makeText(ctx, "Access token expired. Rescan QR code", Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()) {
                    Log.d("Success","Data sent: " + sendCount);
                    sendCount+=1;
                }
            }

            @Override
            public void onFailure(Call<KafkaDataResponse> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }
}
