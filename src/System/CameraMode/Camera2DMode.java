package System.CameraMode;

import System.Camera2D;
import System.Time;

public interface Camera2DMode {
    void apply(Camera2D camera3D, Time elapsed);
}
