package System;


import Graphics.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

public final class Mouse {
    /**
     * Enumeration of all Mouse possible Buttons.
     */
    public enum Button {
        Left(GLFW_MOUSE_BUTTON_1),
        Right(GLFW_MOUSE_BUTTON_2),
        Middle(GLFW_MOUSE_BUTTON_3),
        X1(GLFW_MOUSE_BUTTON_4),
        X2(GLFW_MOUSE_BUTTON_5),

        X3(GLFW_MOUSE_BUTTON_6),
        X4(GLFW_MOUSE_BUTTON_7),
        X5(GLFW_MOUSE_BUTTON_8);

        private final int id;

        Button(int buttonId) {
            this.id = buttonId;
        }
    }

    private boolean hidden = false;
    private boolean grabbed = false;
    private GLFWWindow window;

    /**
     * Generates a Mouse Controler for a specific GLFWWindow
     * @param window mouse monitor
     */
    public Mouse(GLFWWindow window) {
        this.window = window;
    }

    /**
     * Checks if mouse is currently pressed on the GLFWWindow.
     * @param button pressed button
     * @return true if mouse is currently pressed on the GLFWWindow
     */
    public boolean isButtonPressed(Button button) {
        int state = glfwGetMouseButton(window.getGlId(), button.id);
        return (state == GLFW_PRESS);
    }

    /**
     * Gives mouse position into the GLFWWindow.
     * @return local mouse position into the GLFWWindow
     */
    public Vector2f getRelativePosition() {
        double xpos[] = new double[1];
        double ypos[] = new double[1];
        glfwGetCursorPos(window.getGlId(), xpos, ypos);
        return new Vector2f((float)xpos[0], (float)ypos[0]);
    }

    /**
     * Gives mouse position into the Desktop.
     * @return local mouse position into the GLFWWindow
     */
    public Vector2f getAbsolutePosition() {
        return getRelativePosition().add(new Vector2f(window.getPosition()));
    }

    public Vector2f getRelativePositionUsingViewport(Viewport viewport) {
        return (this.getRelativePosition()).add(viewport.getTopLeftCorner().neg());
    }

    public Vector2f getRelativePositionUsingCamera2D(Viewport viewport, Camera2D camera) {
        return getRelativePositionUsingViewport(viewport).add(camera.getCenter().add(camera.getDimension().fact(-0.5f)));
    }

    public void setPosition(Vector2f position){
        glfwSetCursorPos(window.getGlId(), position.x, position.y);
    }

    public void setMouseCursorVisible(boolean state){
        hidden = state;
        glfwSetInputMode(window.getGlId(), GLFW_CURSOR, state ? (grabbed ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL) : GLFW_CURSOR_HIDDEN);
    }

    public void setMouseCursorGrabbed(boolean state){
        grabbed = state;
        glfwSetInputMode(window.getGlId(), GLFW_CURSOR, !state ? (hidden ? GLFW_CURSOR_HIDDEN : GLFW_CURSOR_NORMAL) : GLFW_CURSOR_DISABLED);
    }

}
