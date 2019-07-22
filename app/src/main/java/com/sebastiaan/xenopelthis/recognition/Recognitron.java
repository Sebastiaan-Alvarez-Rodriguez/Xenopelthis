package com.sebastiaan.xenopelthis.recognition;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.sebastiaan.xenopelthis.R;
import com.sebastiaan.xenopelthis.camera.Camera;
import com.sebastiaan.xenopelthis.camera.CameraSelector;
import com.sebastiaan.xenopelthis.camera.CameraStateCallback;
import com.sebastiaan.xenopelthis.camera.exception.NoSuitableCameraException;

import java.util.Collections;


//  search: android camera2 api tutorial simple
//  used: https://inducesmile.com/android/android-camera2-api-example-tutorial/
//  promising: http://coderzpassion.com/android-working-camera2-api/
//  library?:  https://github.com/ravi8x/Barcode-Reader
public class Recognitron extends AppCompatActivity {
    private static final String TAG = "AndroidCameraApi";
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private TextureView textureView;

    private Camera camera;

    private Size imageDimension;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private Handler handler;
    private final Runnable runnable = new Runnable() {
        public void run() {
            takePicture();
            handler.postDelayed(this, DELAY_CAPTURE_MILLISECONDS);
        }
    };
    private static final int DELAY_CAPTURE_MILLISECONDS = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognitron);
        findGlobalViews();
        if (!checkPermission())
            askPermission();
        prepareTexture();

        handler = new Handler();
    }

    private void findGlobalViews() {
        textureView = findViewById(R.id.recognitron_cameraview);
    }

    private void prepareTexture() {
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //open your camera here
                openCamera();
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

    /**
     * Searches through list of camera's for a rear-facing camera
     * @throws NoSuitableCameraException if there is no back-facing camera
     * @throws CameraAccessException if there is a problem accessing cameras
     * @return Camera object for rear-facing camera, null if the CameraManager systemservice was not available
     */
    private @Nullable
    Camera getCamera() throws NoSuitableCameraException, CameraAccessException {
        try {
            CameraSelector s = new CameraSelector(this);
            return s.getFacing(false).get(0);
        } catch (NullPointerException e) {
            return null;
        }
    }

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

        handler.postDelayed(runnable, DELAY_CAPTURE_MILLISECONDS);
    }

    protected void stopBackgroundThread() {
        handler.removeCallbacks(runnable);

        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        Log.e(TAG, "is camera open");
        try {
            camera = getCamera();
            if (camera == null)
                return;
            StreamConfigurationMap map = camera.getCharacteristic(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null)
                return;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (checkPermission()) {
                camera.openCamera(new CameraStateCallback() {
                    @Override
                    public void onOpened() {
                        Log.e("OOOF", "onOpened");
                        render();
                    }
                    @Override
                    public void onClosed() {}

                    @Override
                    public void onDisconnected() {}

                    @Override
                    public void onError(int error){}
                }, null);
            } else {
                Log.e(TAG, "No permission in openCamera()");
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NoSuitableCameraException e) {
            //TODO: Print error to user
        }
        Log.e(TAG, "openCamera X");
    }

    private Surface getTextureSurface() {
        return new Surface(textureView.getSurfaceTexture());
    }

    private void render() {
        SurfaceTexture texture = textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
        Surface surface = new Surface(texture);
        try {
            camera.render(CameraDevice.TEMPLATE_PREVIEW, Collections.singletonList(surface), mBackgroundHandler);
        } catch (CameraAccessException e) {
            camera.closeCamera();
        }
    }

    private void takePicture() {
        try {
            Pair<Integer, Integer> sizes = camera.getMaxHardwareSizeJPEG();
            ImageReader reader = ImageReader.newInstance(sizes.first, sizes.second, ImageFormat.JPEG, 1);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            camera.takePicture(rotation, getTextureSurface(), reader, () -> {
                Log.e(TAG, "I made an image!");
                render();
            }, readerComplete -> {
                //TODO: Do something with picture? Have picture here?
                // readerComplete.acquireLatestImage() .acquireNextImage()?
                // ...
                // also: reader.close();
            }, mBackgroundHandler);
        } catch (Exception ignored) {}
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            prepareTexture();
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        camera.closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Log.e(TAG, "Closing activity since no permission");
                Toast.makeText(Recognitron.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}