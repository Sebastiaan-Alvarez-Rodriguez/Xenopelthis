package com.sebastiaan.xenopelthis.camera;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;

import com.sebastiaan.xenopelthis.camera.exception.NoResponseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to facilitate making pictures with the new camera2 API
 */
public class Camera {
    private static final String TAG = "AndroidCameraApi";

    private String cameraID;
    private CameraCharacteristics characteristics;
    private CameraManager manager;

    private CameraDevice device;

    private Pair<Integer, Integer> resolution = null;

    Camera(String cameraID, CameraCharacteristics characteristics, CameraManager manager) {
        this.cameraID = cameraID;
        this.characteristics = characteristics;
        this.manager = manager;
        device = null;
    }

    /**
     * Function to getLive specific characteristics from the logical camera
     * @param key The specific characteristic to be returned
     * @param <T> The return type of the function (automatically determined)
     * @return Requested characteristic on success, null if not available
     */
    public @Nullable <T> T getCharacteristic(CameraCharacteristics.Key<T> key) {
        return characteristics.get(key);
    }

    /**
     * Function to 'open' a logical camera, in this way getting a hardware camera
     * @param callback Optional callback to getLive callbacks for all possible outcomes of the 'opening'
     * @param handler The handler on which the callback should be invoked, or null to use the current thread's looper
     * @throws CameraAccessException when we could not access the hardware camera
     * @throws SecurityException when we tried to access the hardware camera, while we did not have user permission
     */
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

    /**
     * Function to specify rendertypes and rendertargets
     * @param renderType Type of render, one of the CameraDevice constants, e.g. CameraDevice.TEMPLATE_PREVIEW
     * @param targets The surfaces to render this thing to
     * @param handler The handler on which callbacks are invoked
     * @throws CameraAccessException when we could not access the hardware camera anymore
     */
    public void render(int renderType, List<Surface> targets, Handler handler) throws CameraAccessException {
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
    }

    /**
     * @return a Pair of width and height hardware camera resolution for YUV images, as close to 1080p as possible
     * @throws CameraAccessException if we could not getLive camera characteristics for hardware camera
     * @throws NoResponseException if we did not getLive a CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
     */
    public @NonNull Pair<Integer, Integer> getMaxHardwareSizeYUV() throws CameraAccessException, NoResponseException {
        if (resolution == null)
            calculateMaxHardwareSizeYUV();
        return resolution;
    }

    private void calculateMaxHardwareSizeYUV() throws CameraAccessException, NoResponseException {
        CameraCharacteristics hardwareCharacterstics = manager.getCameraCharacteristics(device.getId());
        StreamConfigurationMap map = hardwareCharacterstics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map == null)
            throw new NoResponseException();

        Size[] jpegSizes = map.getOutputSizes(ImageFormat.YUV_420_888);//TODO: Changed from JPG to YUB_420_888. Still works?
        int width = Integer.MAX_VALUE, height = Integer.MAX_VALUE;

        if (jpegSizes != null && jpegSizes.length > 0) {
            for (Size jpegSize : jpegSizes) {
                if (jpegSize.getWidth() <= width && jpegSize.getWidth() >= 1080) {
                    width = jpegSize.getWidth();
                    height = jpegSize.getHeight();
                    Log.e(TAG, "New: " + width + " x " + height);
                }
                if (width <= 1180)
                    break;
            }
        }
         resolution = new Pair<>(width, height);
    }
    /**
     * This function takes 1 picture
     * @param rotation The rotation of the screen. In actvities: getWindowManager().getDefaultDisplay().getRotation();
     * @param previewSurface The previewsurface to render output to. If we don't do this, we getLive a hanging preview frame
     * @param reader Object reading a second copy of render output. Read image will be accessible with reader.acquireLatestImage()
     * @param callback Callback specifying what we are to do after the picture was taken
     * @param onImageAvailableListener Callback which gets a reader containing the image
     * @param handler The handler on which callbacks are invoked
     */
    public void takePicture(int rotation, Surface previewSurface, ImageReader reader, CameraOnCompletedCallback callback,
                            ImageReader.OnImageAvailableListener onImageAvailableListener, Handler handler) {
        if (device == null) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }

        try {
            CaptureRequest.Builder captureBuilder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(previewSurface);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // Orientation
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

    /**
     * Function that MUST be called when closing a camera-activity. Best practice to close the
     * camera in onPause(), since the camera preview is then gone, and there is no more need to keep
     * hardware busy.
     */
    public void closeCamera() {
        if (device != null) {
            device.close();
            device = null;
        }
    }

    /**
     * Gets the orientation in which the picture should be taken
     * (such that we always display image always aligned with Earth gravity center)
     * @param deviceOrientation The rotation of the screen. In actvities: getWindowManager().getDefaultDisplay().getRotation();
     * @return amount of degrees to turn image
     * @throws CameraAccessException when we could not access the hardware device to getLive sensor data from
     */
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
