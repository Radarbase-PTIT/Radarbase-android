package net.kenevans.baseapi.definitions;

import net.kenevans.baseapi.dto.Auth;

import java.net.URL;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface RefreshTokenService {
    @GET("managementportal/api/meta-token/{token}")
    Call<Auth> getRefreshTokenFromUrl(@Path("token") String tokenName);
}
