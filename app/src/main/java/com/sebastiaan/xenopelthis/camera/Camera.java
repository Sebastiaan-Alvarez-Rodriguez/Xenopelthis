package com.sebastiaan.xenopelthis.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;

import com.sebastiaan.xenopelthis.camera.exception.NoResponseException;

import java.util.ArrayList;
import java.util.List;

public class Camera {
    private static final String TAG = "AndroidCameraApi";

    private String cameraID;
    private CameraCharacteristics characteristics;
    private CameraManager manager;

    private CameraDevice device;

    public Camera(String cameraID, CameraCharacteristics characteristics, CameraManager manager) {
        this.cameraID = cameraID;
        this.characteristics = characteristics;
        this.manager = manager;
        device = null;
    }

    public String getID() {
        return cameraID;
    }

    public @Nullable <T> T getCharacteristic(CameraCharacteristics.Key<T> key) {
        return characteristics.get(key);
    }

    public void openCamera(CameraStateCallback callback, Handler handler) throws CameraAccessException, SecurityException {
        manager.openCamera(cameraID, new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                device = camera;
                callback.onOpened();
            }
            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                device.close();
                callback.onClosed();
                callback.onDisconnected();
            }
            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                device.close();
                device = null;
                callback.onClosed();
                callback.onError(error);
            }
        }, handler);
    }

    public void render(int renderType, List<Surface> targets, Handler handler) {
        try {
            CaptureRequest.Builder captureRequestBuilder = device.createCaptureRequest(renderType);
            for (Surface target : targets)
                captureRequestBuilder.addTarget(target);
            device.createCaptureSession(targets, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (device == null) {
                        return;
                    }
                    captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    try {
                        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, handler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "Camera set in preview-mode!");

                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
//                    Toast.makeText(, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);//TODO: handler null?
        } catch (CameraAccessException e) {

        }
    }

    /**
     * @throws CameraAccessException if we could not get camera characteristics for hardware camera
     * @throws NoResponseException if we did not get a CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
     * @return a Pair of width and height, getting hardware camera resolution
     */
    public @NonNull Pair<Integer, Integer> getMaxHardwareSizeJPEG() throws CameraAccessException, NoResponseException {
            CameraCharacteristics hardwareCharacterstics = manager.getCameraCharacteristics(device.getId());
            StreamConfigurationMap map = hardwareCharacterstics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null)
                throw new NoResponseException();
            Size[] jpegSizes = map.getOutputSizes(ImageFormat.JPEG);
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            return new Pair<>(width, height);
    }

    public void takePicture(Activity activity, Surface previewSurface, ImageReader reader, CameraOnCompletedCallback callback,
                            ImageReader.OnImageAvailableListener onImageAvailableListener, Handler handler) {
        if (device == null) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }

        try {
            CaptureRequest.Builder captureBuilder = device.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation(rotation));

            reader.setOnImageAvailableListener(onImageAvailableListener, handler);

            //TODO: Camera capture happens here
            List<Surface> outputSurfaces = new ArrayList<>();
            outputSurfaces.add(previewSurface);
            outputSurfaces.add(reader.getSurface());

            device.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                            @Override
                            public void onCaptureCompleted(@NonNull CameraCaptureSession session,@NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                                super.onCaptureCompleted(session, request, result);
                                callback.onCompletedCapture();
                            }
                        }, handler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.e(TAG, "Configure failed!");
                }
            }, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        if (device != null) {
            device.close();
            device = null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private int getJpegOrientation(int deviceOrientation) throws CameraAccessException {
        CameraCharacteristics hardwareCharacterstics = manager.getCameraCharacteristics(device.getId());

        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN)
            return 0;
        int sensorOrientation = 0;
        if (hardwareCharacterstics.get(CameraCharacteristics.SENSOR_ORIENTATION) != null)
            sensorOrientation = hardwareCharacterstics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Round device orientation to a multiple of 90
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }
}
