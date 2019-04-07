package System;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

public class Joystick {
    public static boolean isPlugged(){
        return glfwJoystickPresent(GLFW_JOYSTICK_1);
    }
}
