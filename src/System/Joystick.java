package System;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public final class Joystick {
    public enum GamePadButton {
        A(0),
        B(1),
        X(2),
        Y(3),

        CROSS(0),
        CIRCLE(1),
        SQUARE(2),
        TRIANGLE(3),

        LEFT_BUMPER(4),
        RIGHT_BUMPER(5),
        BACK(6),
        START(7),
        LEFT_THUMB(8),
        RIGHT_THUMB(9),
        DPAD_UP(10),
        DPAD_RIGHT(11),
        DPAD_DOWN(12),
        DPAD_LEFT(13),

        LAST(14);


        private int id;
        GamePadButton(int id) {
            this.id = id;
        }
    }
    public enum GamePadAxis {
        R2(5),
        L2(4),
        LEFT_AXIS_X(0),
        LEFT_AXIS_Y(1),
        RIGHT_AXIS_X(2),
        RIGHT_AXIS_Y(3);


        private int id;
        GamePadAxis(int id){
            this.id = id;
        }
    }

    private int id;

    public Joystick(int id) {
        this.id = id;
    }

    public float[] getAllAxis() {
        FloatBuffer axis = glfwGetJoystickAxes(id);

        float[] array = new float[axis.capacity()];
        axis.get(array);

        return array;
    }

    public byte[] getAllButtons(){
        ByteBuffer buttons = glfwGetJoystickButtons(id);

        byte[] array = new byte[buttons.capacity()];
        buttons.get(array);

        return array;
    }

    public boolean isGamePadButtonPressed(GamePadButton button) {
        byte[] b = getAllButtons();
        return !(b == null || button.id >= b.length || button.id < 0) && this.isGamePad() && b[button.id] == GLFW_PRESS;
    }

    public boolean isButtonPressed(int button) {
        byte[] b = getAllButtons();
        return !(b == null || button >= b.length || button < 0) && b[button] == GLFW_PRESS;
    }

    public float getGamePadAxisValue(GamePadAxis axis, float threshold) {
        float[] b = getAllAxis();
        return !(b == null || axis.id >= b.length || axis.id < 0) && isGamePad() ? (Math.abs(b[axis.id]) < threshold ? 0 : b[axis.id]) : 0;
    }

    public float getAxisValue(int axis, float threshold) {
        float[] b = getAllAxis();
        return !(b == null || axis >= b.length || axis < 0) ? (Math.abs(b[axis]) < threshold ? 0 : b[axis])  : 0;
    }

    public boolean isPlugged() {
        return glfwJoystickPresent(id);
    }

    public boolean isGamePad() {
        return glfwJoystickIsGamepad(id);
    }

    public static boolean isPlugged(int id){
        return glfwJoystickPresent(id);
    }

    public static boolean isGamePad(int id){
        return glfwJoystickPresent(id);
    }

}
