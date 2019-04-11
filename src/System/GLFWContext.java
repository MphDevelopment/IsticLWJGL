package System;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

/**
 *
 */
public final class GLFWContext {
    private static int contextCount = 0;

    /**
     * When first context is created we must initialize GLFW
     */
    public static void createContext(){
        if (contextCount++ == 0) {
            // Setup an error callback. The default implementation
            // will print the error message in System.err.
            GLFWErrorCallback.createPrint(System.err).set();

            // Initialize GLFW. Most GLFW functions will not work before doing this.
            if (!glfwInit()) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }
        }
    }

    /**
     * When first context is created we must terminate GLFW
     */
    public static void deleteContext(){
        contextCount--;
        if (contextCount == 0) {
            glfwTerminate();
        }
    }

    private GLFWContext(){}

}
