package System;


import Graphics.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

public class Mouse {
    public enum Button {
        Left(GLFW_MOUSE_BUTTON_1),
        Right(GLFW_MOUSE_BUTTON_2),
        Middle(GLFW_MOUSE_BUTTON_3),
        X1(GLFW_MOUSE_BUTTON_4),
        X2(GLFW_MOUSE_BUTTON_5),

        Z(GLFW_MOUSE_BUTTON_6),
        W(GLFW_MOUSE_BUTTON_7),
        K(GLFW_MOUSE_BUTTON_8);

        public final int id;

        Button(int buttonId) {
            this.id = buttonId;
        }
    }
    private static GLFWWindow context = null;

    private GLFWWindow window;

    public Mouse(GLFWWindow window) {
        this.window = window;
    }

    public boolean isButtonPressed(int key) {
        int state = glfwGetMouseButton(window.getGlId(), key);
        return (state == GLFW_PRESS);
    }

    public Vector2f getPosition() {
        double xpos[] = new double[1];
        double ypos[] = new double[1];
        glfwGetCursorPos(window.getGlId(), xpos, ypos);
        return new Vector2f((float)xpos[0], (float)ypos[0]);
    }

    public void setPosition(Vector2f position){
        glfwSetCursorPos(window.getGlId(), position.x, position.y);
    }

    public static void setContext(GLFWWindow window) {
        context = window;
    }

    public static boolean isButtonPressed_(int button) {
        int state = glfwGetMouseButton(context.getGlId(), button);
        return (state == GLFW_PRESS);
    }

    public static boolean isButtonPressed(int button, GLFWWindow window) {
        int state = glfwGetMouseButton(window.getGlId(), button);
        return (state == GLFW_PRESS);
    }

    public static Vector2f getMousePosition_() {
        double xpos[] = new double[2];
        double ypos[] = new double[2];
        glfwGetCursorPos(context.getGlId(), xpos, ypos);
        return new Vector2f((float)xpos[0], (float)ypos[0]);
    }

    public static Vector2f getMousePosition(GLFWWindow window) {
        double xpos[] = new double[2];
        double ypos[] = new double[2];
        glfwGetCursorPos(window.getGlId(), xpos, ypos);
        return new Vector2f((float)xpos[0], (float)ypos[0]);
    }

    public void setMouseCursorVisible(boolean state){
        glfwSetInputMode(window.getGlId(), GLFW_CURSOR, state ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_HIDDEN);
    }

    public static void setMouseCursorVisible_(boolean state){
        glfwSetInputMode(context.getGlId(), GLFW_CURSOR, state ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_HIDDEN);
    }

    public static void setMouseCursorVisible(GLFWWindow window, boolean state){
        glfwSetInputMode(window.getGlId(), GLFW_CURSOR, state ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_HIDDEN);
    }

    public void setMouseCursorDisabled(boolean state){
        glfwSetInputMode(window.getGlId(), GLFW_CURSOR, !state ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }

    public static void setMouseCursorDisabled_(boolean state){
        glfwSetInputMode(context.getGlId(), GLFW_CURSOR, !state ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }

    public static void setMouseCursorDisabled(GLFWWindow window, boolean state){
        glfwSetInputMode(window.getGlId(), GLFW_CURSOR, !state ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }

}
