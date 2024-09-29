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
import net.kenevans.polar.polarecg.IConstants;
import net.kenevans.polar.polarecg.IQRSConstants;
import net.kenevans.utils.Configurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendDataToKafKa
{
    private String userId;
    private int sendCount;
    private List<Integer> heartRates;
    private List<Double> ecgData;
    private final RetrofitInstanceWithBaseUrl<SendToKafkaService> instance;
    private int measurementTimes;

    public SendDataToKafKa() {
        this.instance = new RetrofitInstanceWithBaseUrl<>(SendToKafkaService.class);
        this.heartRates = new ArrayList<>();
        this.ecgData = new ArrayList<>();
        this.sendCount = 0;
        this.measurementTimes = 0;
        this.userId = "";
    }

    public void run(
            Context ctx,
            String topic,
            PolarEcgData polarEcgData,
            String userId,
            int measurementTimes,
            int heartRate,
            IResponseCallback responseCallback
    ) {

        //Update UI
        responseCallback.onSuccess();

        //for logging number of record stored to check correctness
        sendCount += polarEcgData.samples.size();

        // When measuring nth times and current user is different to previous user using the same phone
        if (measurementTimes != this.measurementTimes || !Objects.equals(userId, this.userId)) {
            this.heartRates = new ArrayList<>();
            this.ecgData = new ArrayList<>();
            this.measurementTimes = measurementTimes;
            this.userId = userId;
        }

        for (Integer value : polarEcgData.samples) {
            //if recording, send samples per 5 seconds each = 73
            if (ecgData.size() == IConstants.KAFKA_TOPIC_POLAR_H10_SENT_INTERVAL * 73) {
                //send data
                send(ctx, topic, measurementTimes);
                this.ecgData = this.ecgData.subList(IConstants.KAFKA_TOPIC_POLAR_H10_SENT_INTERVAL * 73, this.ecgData.size());
                this.heartRates = this.heartRates.subList(IConstants.KAFKA_TOPIC_POLAR_H10_SENT_INTERVAL * 73, this.heartRates.size());
            } else {
                ecgData.add(value * IQRSConstants.MICRO_TO_MILLI_VOLT);
                heartRates.add(heartRate);
            }
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

        instance.getApiService().sendData(topic, headers, data).enqueue(new Callback<KafkaDataResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<KafkaDataResponse> call, @NonNull Response<KafkaDataResponse> response) {
                if (response.code() >= 400) {
                    Toast.makeText(ctx, "Access token expired. Rescan QR code", Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()) {
                    Log.d("Success", "Data sent: " + sendCount);
                }
            }

            @Override
            public void onFailure(Call<KafkaDataResponse> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }
}
