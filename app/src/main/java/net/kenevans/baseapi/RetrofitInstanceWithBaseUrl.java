package net.kenevans.baseapi;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInstanceWithBaseUrl<T> {

    private final T apiService;

    public RetrofitInstanceWithBaseUrl(Class<T> retrofitClass) {

        // Create and set up the logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

// Add the interceptor to the OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.109/") // API Base URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON Converter
                .build();

        this.apiService = retrofit.create(retrofitClass);
    }

    public T getApiService() {
        return apiService;
    }
}
