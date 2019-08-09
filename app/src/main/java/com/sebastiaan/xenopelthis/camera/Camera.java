package com.sebastiaan.xenopelthis.camera;

import android.util.Size;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

/**
 * Class to facilitate making pictures with the new cameraX API
 */
//https://android.jlelse.eu/an-introduction-to-camerax-7ca657e6eee0
//https://proandroiddev.com/android-camerax-preview-analyze-capture-1b3f403a9395
public class Camera {
    private static final String TAG = "AndroidCameraApi";

    /**
     * Function to open a logical camera, and create a preview stream usecase
     * @param textureView The textureview to receive previews
     * @param preferredResolution  the preferred rendering resolution (may differ depending on device capabilities)
     * @throws SecurityException when we tried to access the hardware camera, while we did not have user permission
     */
    public Preview render(@NonNull TextureView textureView, @NonNull Size preferredResolution) throws SecurityException {
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setLensFacing(CameraX.LensFacing.BACK)
                .setTargetResolution(preferredResolution)
                .build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(output -> textureView.setSurfaceTexture(output.getSurfaceTexture()));
        return preview;
    }

    public Preview render(@NonNull TextureView textureView) throws SecurityException {
        return this.render(textureView, new Size(800,600));
    }

    public ImageAnalysis analyse(@NonNull ImageAnalysis.Analyzer analyzer, @NonNull Size preferredResolution) {
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder()
                .setTargetResolution(preferredResolution)
//                .setTargetRotation
//                .setCallbackHandler() maybe? https://codelabs.developers.google.com/codelabs/camerax-getting-started/#7
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig);

        imageAnalysis.setAnalyzer(analyzer);
        return imageAnalysis;
    }

    public ImageAnalysis analyse(@NonNull ImageAnalysis.Analyzer analyzer) {
        return this.analyse(analyzer, new Size(800,600));
    }

//    /**
//     * Gets the orientation in which the picture should be taken
//     * (such that we always display image always aligned with Earth gravity center)
//     * @param deviceOrientation The rotation of the screen. In actvities: getWindowManager().getDefaultDisplay().getRotation();
//     * @return amount of degrees to turn image
//     * @throws CameraAccessException when we could not access the hardware device to getLive sensor data from
//     */
//    @SuppressWarnings("ConstantConditions")
//    private int getJpegOrientation(int deviceOrientation) throws CameraAccessException {
//        CameraCharacteristics hardwareCharacterstics = manager.getCameraCharacteristics(device.getId());
//
//        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN)
//            return 0;
//        int sensorOrientation = 0;
//        if (hardwareCharacterstics.get(CameraCharacteristics.SENSOR_ORIENTATION) != null)
//            sensorOrientation = hardwareCharacterstics.get(CameraCharacteristics.SENSOR_ORIENTATION);
//
//        // Round device orientation to a multiple of 90
//        deviceOrientation = (deviceOrientation + 45) / 90 * 90;
//
//        // Calculate desired JPEG orientation relative to camera orientation to make
//        // the image upright relative to the device orientation
//        return (sensorOrientation + deviceOrientation + 360) % 360;
//    }
}
