package System.CameraMode;

import System.Keyboard;
import Graphics.Vector3f;
import System.Camera3D;

import System.Time;

import static org.lwjgl.glfw.GLFW.*;


public class FPSCamera3DMode implements Camera3DMode {
    private Vector3f _angles = new Vector3f(0,0,0);
    private Keyboard keyboard;

    public FPSCamera3DMode(Keyboard keyboard){
        this.keyboard = keyboard;
    }

    public FPSCamera3DMode(Keyboard keyboard, Vector3f angles) {
        this.keyboard = keyboard;
        this._angles = new Vector3f(angles);
    }

    @Override
    public void apply(Camera3D camera, Time elapsed) {
        final int pxPerSeconds = 200;
        final double radPerSeconds = Math.PI;

        double seconds = elapsed.asSeconds();

        // movement handling
        Vector3f pos = camera.getCenter();
        Vector3f motion = new Vector3f();
        if (keyboard.isKeyPressed(GLFW_KEY_Z)) {
            motion.add(camera.getOrientation().fact((float)( pxPerSeconds*seconds)));
        }
        if (keyboard.isKeyPressed(GLFW_KEY_S)) {
            motion.add(camera.getOrientation().fact((float)(-pxPerSeconds*seconds)));
        }
        if (keyboard.isKeyPressed(GLFW_KEY_Q)) {
            motion.add(Vector3f.product(camera.getUpVector(), camera.getOrientation()).mul((float)(-pxPerSeconds*seconds)));
        }
        if (keyboard.isKeyPressed(GLFW_KEY_D)) {
            motion.add(Vector3f.product(camera.getUpVector(), camera.getOrientation()).mul((float)( pxPerSeconds*seconds)));
        }
        if (keyboard.isKeyPressed(GLFW_KEY_SPACE)) {
            motion.add(camera.getUpVector().mul((float)(pxPerSeconds*seconds)));
        }
        if (keyboard.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            motion.add(camera.getUpVector().neg().mul((float)(pxPerSeconds*seconds)));
        }
        if (motion.compareTo(new Vector3f(0,0,0)) != 0) {
            motion.normalize().fact((float) (pxPerSeconds * seconds));
            pos.add(motion);
            camera.setPosition(pos);
        }

        // angle handling
        if (keyboard.isKeyPressed(GLFW_KEY_UP)) {
            _angles.z += radPerSeconds*seconds;
        }
        if (keyboard.isKeyPressed(GLFW_KEY_DOWN)) {
            _angles.z -= radPerSeconds*seconds;
        }
        if (keyboard.isKeyPressed(GLFW_KEY_LEFT)) {
            _angles.y -= radPerSeconds*seconds;
        }
        if (keyboard.isKeyPressed(GLFW_KEY_RIGHT)) {
            _angles.y += radPerSeconds*seconds;
        }
        if (_angles.z < -3.14 / 2)
            _angles.z = -3.14f / 2;
        if (_angles.z > 3.14 / 2)
            _angles.z = 3.14f / 2;

        camera.look(radianToDirection(_angles));
    }

    public static Vector3f radianToDirection(Vector3f v) {
        return new Vector3f((float)(Math.cos(v.z)*Math.cos(v.y)), (float)(Math.sin(v.z)), (float)(Math.cos(v.z)*Math.sin(v.y)));
    }

}
