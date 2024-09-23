package net.kenevans.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import net.kenevans.polar.polarecg.SimpleScannerActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class POSTResourceToURL extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
    @SuppressLint("StaticFieldLeak")
    private SimpleScannerActivity simpleScannerActivity;

    public POSTResourceToURL(SimpleScannerActivity simpleScannerActivity) {
        this.simpleScannerActivity = simpleScannerActivity;
    }

    protected JSONObject doInBackground(HashMap<String, String>... params) {
        HashMap<String, String> urlMap = params[0];
        HashMap<String, String> dataMap = params[1];
         // Data to POST
        JSONObject jsonObject = new JSONObject();

        HttpURLConnection urlConnection = null;

        StringBuilder response = new StringBuilder();

        try {
            // Create the URL object
            URL url = new URL(urlMap.get("url"));
            JSONObject jsonData = new JSONObject(dataMap);
            String jsonBody = jsonData.toString();

            // Open connection
            urlConnection = (HttpURLConnection) url.openConnection();

            // Set up request
            urlConnection.setRequestMethod("POST");

            // Set headers if needed (Optional)
            urlConnection.setRequestProperty("Content-Type", "application/json");

            // Enable output to send data
            urlConnection.setDoOutput(true);

            // Send data (write data into the request body)
            OutputStream os = urlConnection.getOutputStream();
            os.write(jsonBody.getBytes("UTF-8"));
            os.flush();
            os.close();
            // Get response code
            int responseCode = urlConnection.getResponseCode();

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
        simpleScannerActivity.postAccessToken(result);
    }
}
