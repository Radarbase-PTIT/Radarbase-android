package net.kenevans.baseapi.definitions;

import net.kenevans.baseapi.dto.KafkaDataResponse;
import net.kenevans.baseapi.requests.PolarH10ECG;
import net.kenevans.utils.Configurations;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SendToKafkaService {

    @POST("kafka/topics/{topic}")
    Call<KafkaDataResponse> sendData(
            @Path("topic") String topic,
            @HeaderMap Map<String, String> headerMap,
            @Body PolarH10ECG data);
}
