package net.kenevans.baseapi.definitions;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MockService {

    @POST("/posts")
    Call<Boolean> mockTrue(@Body HashMap<String, String> body);
}
