package com.sebastiaan.xenopelthis.camera;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

import com.sebastiaan.xenopelthis.camera.exception.NoSuitableCameraException;

import java.util.ArrayList;
import java.util.List;

public class CameraSelector {
    private CameraManager manager;

    public CameraSelector(Context context) {
        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (manager == null)
            throw new NullPointerException();
    }

    public List<Camera> getFacing(boolean front) throws CameraAccessException, NoSuitableCameraException {

        List<Camera> cameras = new ArrayList<>();
        for (String id : manager.getCameraIdList()) {
            CameraCharacteristics c = manager.getCameraCharacteristics(id);

            Integer resp = c.get(CameraCharacteristics.LENS_FACING);

            if (resp != null && ((front && resp == CameraCharacteristics.LENS_FACING_FRONT) || (!front && resp == CameraCharacteristics.LENS_FACING_BACK)))
                cameras.add(new Camera(id, c, manager));
        }
        if (cameras.isEmpty())
            throw new NoSuitableCameraException();
        return cameras;
    }
}
