package net.kenevans.baseapi.dto;


public class AccessTokenOutput {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private String expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() { return refresh_token; }

    public String getExpires_in() {
        return expires_in;
    }

    public String[] getSources() {
        return sources;
    }

    public String getSub() {
        return sub;
    }

    private String[] sources;

    private String sub;
}
