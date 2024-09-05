package net.kenevans.polar.polarecg;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import net.kenevans.util.GETResourceFromURL;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int CAMERA_PERMISSION_CODE = 1;
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        //check permission to barcode scanner
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        setContentView(R.layout.simple_scanner_activity);
        mScannerView = findViewById(R.id.scannerView);   // Programmatically initialize the scanner view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
        releaseCamera();
    }

    private void releaseCamera() {
        mScannerView.stopCameraPreview();
        mScannerView = null;
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        String radarbaseUrl = rawResult.getText();

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
        try {
            URL url = new URL(radarbaseUrl);
            new GETResourceFromURL().execute(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseJSONFromAsyncTask(JSONObject jsonObject) {
        String TAG = "scanner";
        Log.d(TAG,jsonObject.toString());
    }
}
