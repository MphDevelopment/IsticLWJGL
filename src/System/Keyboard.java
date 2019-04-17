package System;


import java.awt.im.InputContext;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    public static final int AZERTY = 0;
    public static final int QWERTY = 1;
    public static final int QWERTZ = 2;

    /**Default context values can be use if only one window is generated*/
    /*private static GLFWWindow context = null;
    private static String defaultLayout = AZERTY;*/

    /**Context values*/
    private GLFWWindow window;
    private int layout = QWERTY;

    /**
     * Generates a keyboard associated to a window.
     * Default layout is QWERTY.
     * @param window the keyboard will be associated to this window
     */
    public Keyboard(GLFWWindow window) {
        this.window = window;
    }

    /**
     * Generates a keyboard associated to a window using a specific layout.
     * If failed to set layout, the default layout become QWERTY.
     * @param window the keyboard will be associated to this window
     */
    public Keyboard(GLFWWindow window, int layout) {
        this.window = window;
        try {
            this.setLayout(layout);
        } catch (IOException e) {
            this.layout = QWERTY;
            e.printStackTrace();
        }
    }

    /**
     * Checks if the keyboard key is pressed or not
     * @param key tested keyboard key
     * @return true if the keyboard key is pressed else false
     */
    public boolean isKeyPressed(int key) {
        int state = glfwGetKey(window.getGlId(), toLocalLayout(layout, key));
        return (state == GLFW_PRESS);
    }

    /**
     * Applies a new keyboard layout to specific country keyboard
     * @param layout chosen keyboard layout
     * @throws IOException thrown when the keyboard layout is not well known
     */
    public void setLayout(int layout) throws IOException {
        int[] layouts = getLocalLayouts();
        for (int i=0 ; i < layouts.length ; ++i) {
            if (layouts[i] == layout) {
                this.layout = layout;
                return ;
            }
        }

        throw new IOException("Unknown keyboard layout");
    }

    /**
     * List of layout containing some keyboard default layout as AZERTY or QWERTY
     * @return keyboard key layout depending on which country the user is
     */
    private static int[] getLocalLayouts() {
        return new int[]{AZERTY, QWERTY, QWERTZ};
    }

    private static int toLocalLayout(int layout, int key) {
        switch (layout) {
            case AZERTY: return AzertyToQwerty(key);
            case QWERTY: return toQwertyLayout(key);
            case QWERTZ: return QwertzToQwerty(key);
            default: return key;
        }
    }

    private static int toQwertyLayout(int glfwKeyCode) {
        return glfwKeyCode;
    }

    private static int QwertzToQwerty(int glfwKeyCode) {
        switch( glfwKeyCode ) {
            case GLFW_KEY_Z             : return GLFW_KEY_Y;
            case GLFW_KEY_Y             : return GLFW_KEY_Z;
            default                     : return glfwKeyCode;
        }
    }

    private static int AzertyToQwerty(int qwertyGlfwKeyCode) {
        switch( qwertyGlfwKeyCode ) {
            //local                       qwerty
            case GLFW_KEY_A             : return GLFW_KEY_Q;
            case GLFW_KEY_Q             : return GLFW_KEY_A;
            case GLFW_KEY_W             : return GLFW_KEY_Z;
            case GLFW_KEY_Z             : return GLFW_KEY_W;

            //case GLFW_KEY_M             : return GLFW_KEY_COMMA;
            case GLFW_KEY_M             : return GLFW_KEY_SEMICOLON;
            case GLFW_KEY_COMMA         : return GLFW_KEY_M;

            default                     : return qwertyGlfwKeyCode;
        }
    }

}

/**
 * Sets up a new GLFW context for keyboard listening
 * @param window the keyboard will be associated to this window
 */
    /*public static void setContext(GLFWWindow window) {
        //System.out.println("Keyboard input context:"+InputContext.getInstance().getLocale());
        context = window;
    }*/

/**
 * Checks if the keyboard key is pressed or not
 * @param key tested keyboard key
 * @return true if the keyboard key is pressed else false
 */
    /*public static boolean isKeyPressed_(int key) {
        int state = glfwGetKey(context.getGlId(), toLocalLayout(defaultLayout, key));
        return (state == GLFW_PRESS);
    }*/

/**
 * Checks if the keyboard key is pressed or not
 * @param key tested keyboard key
 * @param window the keyboard is associated to this window
 * @return true if the keyboard key is pressed else false
 */
    /*public static boolean isKeyPressed(int key, GLFWWindow window) {
        int state = glfwGetKey(window.getGlId(), toLocalLayout(defaultLayout, key));
        return (state == GLFW_PRESS);
    }*/

/**
 * Applies a new keyboard layout to specific country keyboard
 * @param layout chosen keyboard layout
 * @throws IOException thrown when the keyboard layout is not well known
 */
    /*public static void setDefaultLayout(String layout) throws IOException {
        String[] layouts = getLocalLayouts();
        for (int i=0 ; i < layouts.length ; ++i) {
            if (layouts[i].equals(layout)) {
                defaultLayout = layout;
                return ;
            }
        }

        throw new IOException("Unknown keyboard layout");
    }*/
