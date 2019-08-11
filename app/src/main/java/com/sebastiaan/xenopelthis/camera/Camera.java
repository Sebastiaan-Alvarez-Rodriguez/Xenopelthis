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
     * Function to open a logical camera, and create a preview stream use case
     * @param textureView The textureview to receive previews
     * @param preferredResolution  The resolution of stream frames. The actual resolution may differ,
     *                             because of hardware capabilities
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

    /**
     * @see #render(TextureView, Size)
     * Works just like this method, with fixed preferredResolution 800 x 600
     */
    public Preview render(@NonNull TextureView textureView) throws SecurityException {
        return this.render(textureView, new Size(800,600));
    }

    /**
     * Creates an analysis stream use case (stream of YUV_420_888)
     * @param analyzer The analyzer which tells what to do with each analysis frame
     * @param preferredResolution The resolution of stream frames. The actual resolution may differ,
     *                            because of hardware capabilities
     * @return an ImageAnalysis use case
     */
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

    /**
     * @see #analyse(ImageAnalysis.Analyzer, Size)
     * Works just like this method, with fixed preferredResolution 800 x 600
     */
    public ImageAnalysis analyse(@NonNull ImageAnalysis.Analyzer analyzer) {
        return this.analyse(analyzer, new Size(800,600));
    }
}
