package net.kenevans.apiservice;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import net.kenevans.baseapi.RetrofitInstanceWithBaseUrl;
import net.kenevans.apiservice.definitions.SchemaSubjectTopicService;
import net.kenevans.apiservice.dto.SchemaSubject;
import net.kenevans.utils.Configurations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreSchemaSubjectTopicId {

    public static void run(Context ctx, String topicName) {
        RetrofitInstanceWithBaseUrl<SchemaSubjectTopicService> instance = new RetrofitInstanceWithBaseUrl<>(SchemaSubjectTopicService.class);
        instance.getApiService().getTopicId(topicName).enqueue(new Callback<SchemaSubject>() {
            @Override
            public void onResponse(@NonNull Call<SchemaSubject> call, @NonNull Response<SchemaSubject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String id = "" + response.body().getId();
                    Configurations.updatePreference(ctx, topicName, id);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SchemaSubject> call, @NonNull Throwable t) {
                Log.e("ERROR", "Check network connection");
            }
        });

    }
}
