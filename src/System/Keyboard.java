package System;


import java.awt.im.InputContext;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    public static final String AZERTY = "AZERTY";
    public static final String QWERTY = "QWERTY";
    public static final String QWERTZ = "QWERTZ";

    /**Default context values can be use if only one window is generated*/
    private static GLFWWindow context = null;
    private static String defaultLayout = AZERTY;

    /**Context values*/
    private GLFWWindow window;
    private String layout = AZERTY;

    /**
     * Generates a keyboard associated to a window
     * @param window the keyboard will be associated to this window
     */
    public Keyboard(GLFWWindow window) {
        this.window = window;
    }

    /**
     * Checks if the keyboard key is pressed or not
     * @param key tested keyboard key
     * @return true if the keyboard key is pressed else false
     */
    public boolean isKeyPressed(int key) {
        int state = glfwGetKey(window.getGlId(), key);
        return (state == GLFW_PRESS);
    }

    /**
     * Applies a new keyboard layout to specific country keyboard
     * @param layout chosen keyboard layout
     * @throws IOException thrown when the keyboard layout is not well known
     */
    public void setLayout(String layout) throws IOException {
        String[] layouts = getLocalLayouts();
        for (int i=0 ; i < layouts.length ; ++i) {
            if (layouts[i].equals(layout)) {
                this.layout = layout;
                return ;
            }
        }

        throw new IOException("Unknown keyboard layout");
    }

    /**
     * Sets up a new GLFW context for keyboard listening
     * @param window the keyboard will be associated to this window
     */
    public static void setContext(GLFWWindow window) {
        System.out.println("Keyboard input context:"+InputContext.getInstance().getLocale());
        context = window;
    }

    /**
     * Checks if the keyboard key is pressed or not
     * @param key tested keyboard key
     * @return true if the keyboard key is pressed else false
     */
    public static boolean isKeyPressed_(int key) {
        int state = glfwGetKey(context.getGlId(), toLocalLayout(defaultLayout, key));
        return (state == GLFW_PRESS);
    }

    /**
     * Checks if the keyboard key is pressed or not
     * @param key tested keyboard key
     * @param window the keyboard is associated to this window
     * @return true if the keyboard key is pressed else false
     */
    public static boolean isKeyPressed(int key, GLFWWindow window) {
        int state = glfwGetKey(window.getGlId(), toLocalLayout(defaultLayout, key));
        return (state == GLFW_PRESS);
    }

    /**
     * Applies a new keyboard layout to specific country keyboard
     * @param layout chosen keyboard layout
     * @throws IOException thrown when the keyboard layout is not well known
     */
    public static void setDefaultLayout(String layout) throws IOException {
        String[] layouts = getLocalLayouts();
        for (int i=0 ; i < layouts.length ; ++i) {
            if (layouts[i].equals(layout)) {
                defaultLayout = layout;
                return ;
            }
        }

        throw new IOException("Unknown keyboard layout");
    }

    /**
     * List of layout containing some keyboard default layout as AZERTY or QWERTY
     * @return keyboard key layout depending on which country the user is
     */
    private static String[] getLocalLayouts() {
        return new String[]{AZERTY, QWERTY, QWERTZ};
    }

    private static int toLocalLayout(String layout, int key) {
        if (layout.equals("AZERTY")) {
            return toAzertyLayout(key);
        } else if (layout.equals("QWERTY")) {
            return toQwertyLayout(key);
        } else if (layout.equals("QWERTZ")) {
            return toQwertzLayout(key);
        } else {
            return 0;
        }
    }

    private static int toQwertyLayout(int glfwKeyCode) {
        return glfwKeyCode;
    }

    private static int toQwertzLayout(int glfwKeyCode) {
        switch( glfwKeyCode ) {
            case GLFW_KEY_Z             : return GLFW_KEY_Y;
            case GLFW_KEY_Y             : return GLFW_KEY_Z;
            default                     : return glfwKeyCode;
        }
    }

    private static int toAzertyLayout(int glfwKeyCode) {
        switch( glfwKeyCode ) {
            case GLFW_KEY_A             : return GLFW_KEY_Q;
            case GLFW_KEY_Q             : return GLFW_KEY_A;
            case GLFW_KEY_W             : return GLFW_KEY_Z;
            case GLFW_KEY_Z             : return GLFW_KEY_W;
            // ....... and so one
            default                     : return glfwKeyCode;
        }
    }

}
