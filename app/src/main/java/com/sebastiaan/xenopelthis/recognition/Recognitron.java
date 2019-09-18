package com.sebastiaan.xenopelthis.recognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.core.app.ActivityCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.camera.Camera;


/**
 * Activity to scan for barcodes.
 * On success, sets RESULT_OK flag and puts raw barcode value in returned intent, under key 'barcode'
 */
//  search: android camera2 api tutorial simple
//  used: https://inducesmile.com/android/android-camera2-api-example-tutorial/
//  promising: http://coderzpassion.com/android-working-camera2-api/
// For fix slowness: https://android.jlelse.eu/the-least-you-can-do-with-camera2-api-2971c8c81b8b
public class Recognitron extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private TextureView textureView;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognitron);
        FirebaseApp.initializeApp(getApplicationContext());
        findGlobalViews();
        if (!checkPermission()) {
            askPermission();
        } else {
            openCamera();
            CameraX.bindToLifecycle(this,
                    render(),
                    analyse());
        }
    }

    /**
     * Locates necessary member views in the view tree
     */
    private void findGlobalViews() {
        textureView = findViewById(R.id.recognitron_cameraview);
    }

    /**
     * Checks whether this app has permission to use the camera
     * @return true if this app has camera permission, otherwise false
     */
    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Asks permission to use the camera
     */
    private void askPermission() {
        ActivityCompat.requestPermissions(Recognitron.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    /**
     * Constructs a camera object to use
     */
    private void openCamera() {
        camera = new Camera();
    }

    /**
     * Constructs a Preview use case (to render)
     * @return the Preview use case to render
     */
    private Preview render() {
        return camera.render(textureView);
    }

    /**
     * Constructs an ImageAnalysis use case, to analyse frames (YUV_420_888 type)
     * @return the ImageAnalysis use case
     */
    private ImageAnalysis analyse() {
        return camera.analyse((image, rotationDegrees) -> interpretResult(image.getImage(), rotationDegrees));
        //TODO: copy-construct image to reduce load on imaging pipeline?
    }

    /**
     * Tries to read a barcode. Exits the activity on success (and sets RESULT_OK flag)
     * @param picture the Image to be scanned for barcodes.
     * @param rotation the rotation, in [0-360), which tells by how many degrees the image should be rotated to match reality
     */
    private void interpretResult(@Nullable Image picture, @FirebaseVisionImageMetadata.Rotation int rotation) {
        if (picture == null)
            return;

        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(picture, 0);
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
        detector.detectInImage(image)
                .addOnSuccessListener(firebaseVisionBarcodes -> {
                    if (!firebaseVisionBarcodes.isEmpty()) {
                        Intent intent = new Intent();
                        intent.putExtra("barcode", firebaseVisionBarcodes.get(0).getDisplayValue());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(Recognitron.this, "You can't scan without granting camera permission", Toast.LENGTH_LONG).show();
                finish();
            } else {
//                openCamera();
//                CameraX.bindToLifecycle(this,
//                        render(),
//                        analyse());
                recreate();
            }
        }
    }
}