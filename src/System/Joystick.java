package System;

import Graphics.Vector2f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Joystick {
    private int id;

    public Joystick(int id) {

    }

    public Vector2f[] getAllAxis() {
        FloatBuffer axis = glfwGetJoystickAxes(id);
        if (axis != null) {
            float[] array = new float[axis.capacity()];
            axis.get(array);
        }
        return null;
        //return new Vector2f(array[0], array[1]);
    }

    public boolean[] getAllButtons(){
        ByteBuffer buttons = glfwGetJoystickButtons(id);
        return null;
    }

    public static boolean isPlugged(int id){
        return glfwJoystickPresent(id);
    }

}
