package com.sebastiaan.xenopelthis.recognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.camera.Camera;


//  search: android camera2 api tutorial simple
//  used: https://inducesmile.com/android/android-camera2-api-example-tutorial/
//  promising: http://coderzpassion.com/android-working-camera2-api/
// For fix slowness: https://android.jlelse.eu/the-least-you-can-do-with-camera2-api-2971c8c81b8b
public class Recognitron extends AppCompatActivity {
    private static final String TAG = "AndroidCameraApi";
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private TextureView textureView;

    private Camera camera;

    private static final int DELAY_CAPTURE_MILLISECONDS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognitron);
        FirebaseApp.initializeApp(getApplicationContext());
        findGlobalViews();
        if (!checkPermission())
            askPermission();
        else
//            prepareTexture();
            openCamera();
            CameraX.bindToLifecycle((LifecycleOwner) this,
                    render(),
                    analyse());
    }

    private void findGlobalViews() {
        textureView = findViewById(R.id.recognitron_cameraview);
    }

    private void prepareTexture() {
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //open your camera here

            }
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // Transform you image captured size according to the surface width and height
            }
            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });
    }

    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(Recognitron.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }


    private void openCamera() {
        Log.e(TAG, "is camera open");
        camera = new Camera();
    }

    private Preview render() {
        return camera.render(textureView);
    }

    private ImageAnalysis analyse() {
        return camera.analyse((image, rotationDegrees) -> interpretResult(image.getImage(), rotationDegrees));
        //TODO: copy-construct image to reduce load on imaging pipeline?
    }

    private void interpretResult(@Nullable Image picture, @FirebaseVisionImageMetadata.Rotation int rotation) {
        if (picture == null)
            return;

        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(picture, 0);
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
        detector.detectInImage(image)
                .addOnSuccessListener(firebaseVisionBarcodes -> {
                    for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes) {
                        Log.e("BAR_RECOGNISED", "Got something!!!!!!!");
                        Log.e("BAR_RECOGNISED", "Raw: "+ barcode.getRawValue());
                        Log.e("BAR_RECOGNISED", "Display: "+barcode.getDisplayValue());
                        Log.e("BAR_RECOGNISED", "Format: "+barcode.getFormat());
                    }
                    if (!firebaseVisionBarcodes.isEmpty()) {
                        Intent intent = new Intent();
                        intent.putExtra("barcode", firebaseVisionBarcodes.get(0).getDisplayValue());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("BAR_FAILED", "Failure: "+e.getMessage());
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(Recognitron.this, "Sorry!!!, you can't scan without granting camera permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}