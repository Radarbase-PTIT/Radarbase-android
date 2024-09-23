package net.kenevans.baseapi.definitions;

import net.kenevans.baseapi.dto.AccessTokenOutput;
import net.kenevans.baseapi.dto.Auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccessTokenService {

    class AccessTokenBody {
        private String grant_type;
        private String refresh_token;
        private String client_id;

        public String getGrant_type() {
            return grant_type;
        }

        public void setGrant_type(String grant_type) {
            this.grant_type = grant_type;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getClient_secret() {
            return client_secret;
        }

        public void setClient_secret(String client_secret) {
            this.client_secret = client_secret;
        }

        private String client_secret;

        public AccessTokenBody(String grant_type, String refresh_token, String client_id, String client_secret) {
            this.grant_type = grant_type;
            this.refresh_token = refresh_token;
            this.client_id = client_id;
            this.client_secret = client_secret;
        }


    }

    @FormUrlEncoded
    @POST("managementportal/oauth/token")
    Call<AccessTokenOutput> retrieveAccessTokenFromRefreshToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret
    );
}
