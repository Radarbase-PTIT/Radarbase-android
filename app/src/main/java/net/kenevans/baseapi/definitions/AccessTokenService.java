package net.kenevans.baseapi.definitions;

import net.kenevans.baseapi.dto.AccessTokenOutput;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AccessTokenService {

    @FormUrlEncoded
    @POST("managementportal/oauth/token")
    Call<AccessTokenOutput> retrieveAccessTokenFromRefreshToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret
    );
}
