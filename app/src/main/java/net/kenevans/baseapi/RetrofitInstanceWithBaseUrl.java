package net.kenevans.baseapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstanceWithBaseUrl<T> {

    private final T apiService;

    public RetrofitInstanceWithBaseUrl(Class<T> retrofitClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.109/") // API Base URL
                .addConverterFactory(GsonConverterFactory.create()) // JSON Converter
                .build();

        this.apiService = retrofit.create(retrofitClass);
    }

    public T getApiService() {
        return apiService;
    }
}
