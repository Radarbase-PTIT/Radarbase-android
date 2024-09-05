package net.kenevans.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import net.kenevans.polar.polarecg.SimpleScannerActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GETResourceFromURL extends AsyncTask<URL, Integer, JSONObject> {
    @SuppressLint("StaticFieldLeak")
    private SimpleScannerActivity simpleScannerActivity;

    public GETResourceFromURL(SimpleScannerActivity simpleScannerActivity) {
        this.simpleScannerActivity = simpleScannerActivity;
    }

    protected JSONObject doInBackground(URL... urls) {
        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();
        JSONObject jsonObject = null;

        try {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                Log.d("Scan", url.toString());
                // Open connection
                urlConnection = (HttpURLConnection) url.openConnection();

                // Set up request
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json"); // Optional: Specify response type
//
//                // Get response code
                int responseCode = urlConnection.getResponseCode();
                Log.d("Scan", "Response code" +responseCode);
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

                else {
                    jsonObject = null;
                }
            }

        } catch (Exception e) {
            jsonObject = null;
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
        if (simpleScannerActivity != null) {
            simpleScannerActivity = new SimpleScannerActivity();
            simpleScannerActivity.parseJSONFromAsyncTask(result);
        } else {
            Toast.makeText(simpleScannerActivity, "QRCode expired or QR Code error", Toast.LENGTH_SHORT).show();
        }
    }
}
