package System.CameraMode;

import System.Camera3D;
import System.Time;

public interface Camera3DMode {
    void apply(Camera3D camera3D, Time elapsed);
}
