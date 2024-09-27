package net.kenevans.baseapi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitMockInstanceWithBaseUrl<T> {

    private final T apiService;

    public RetrofitMockInstanceWithBaseUrl(Class<T> retrofitClass) {

        // Create and set up the logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

// Add the interceptor to the OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/") // This won't actually be called
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON Converter
                .build();

        this.apiService = retrofit.create(retrofitClass);
    }

    public T getApiService() {
        return apiService;
    }
}
