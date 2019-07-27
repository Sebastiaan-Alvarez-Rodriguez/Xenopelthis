package com.sebastiaan.xenopelthis.camera;

public interface CameraStateCallback {
    void onOpened();
    void onClosed();
    void onDisconnected();
    void onError(int error);
}
