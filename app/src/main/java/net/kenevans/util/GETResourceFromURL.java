package net.kenevans.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import net.kenevans.polar.polarecg.SimpleScannerActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class GETResourceFromURL extends AsyncTask<URL, Integer, JSONObject> {
    @SuppressLint("StaticFieldLeak")
    private SimpleScannerActivity simpleScannerActivity;

    public GETResourceFromURL(SimpleScannerActivity simpleScannerActivity) {
        this.simpleScannerActivity = simpleScannerActivity;
    }

    protected JSONObject doInBackground(URL... urls) {
        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();
        JSONObject jsonObject = new JSONObject();

        try {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                // Open connection
                urlConnection = (HttpURLConnection) url.openConnection();

                // Set up request
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json"); // Optional: Specify response type
//
//                // Get response code
                int responseCode = urlConnection.getResponseCode();
//
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read response
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    jsonObject = new JSONObject(response.toString());
                }
            }

        } catch (Exception e) {
            Log.e("Scan error", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return jsonObject;
    }


    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        //get access Token

        HashMap<String, String> url = new HashMap<>();
        String getAccessTokenUrl = "http://192.168.1.109/managementportal/oauth/token";
        url.put("url", getAccessTokenUrl);
        try {
            //Get data
            HashMap<String, String> data = new HashMap<>();
            data.put("grant_type", "refresh_token");
            data.put("client_id", "pRMT");
            if (result.has("refreshToken")) {
                String refreshToken = result.getString("refreshToken");
                data.put("refresh_token", refreshToken);
            }
            data.put("client_secret", "saturday$SHARE$scale");

            new POSTResourceToURL(this.simpleScannerActivity).execute(url,data);
        } catch (Exception e) {

        }
    }
}
