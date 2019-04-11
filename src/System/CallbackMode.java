package System;


import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * CallbackMode provides interface to allow or not some GLFWWindow Callback modes
 */
public class CallbackMode {
    /**
     * Default callback modes coming from GLFW lib
     */
    public static CallbackMode DEFAULT = new CallbackMode(~0);
    public static final CallbackMode KEY = new CallbackMode(1 << 0);
    public static final CallbackMode CHAR = new CallbackMode(1 << 1);
    public static final CallbackMode CURSORENTER = new CallbackMode(1 << 2);
    public static final CallbackMode BUTTON = new CallbackMode(1 << 3);
    public static final CallbackMode SCROLL = new CallbackMode(1 << 4);
    public static final CallbackMode MOUSEDROP = new CallbackMode(1 << 5);
    public static final CallbackMode JOYSTICK = new CallbackMode(1 << 6);
    public static final CallbackMode RESIZE = new CallbackMode(1 << 7);
    public static final CallbackMode MOVE = new CallbackMode(1 << 8);
    public static final CallbackMode FOCUS = new CallbackMode(1 << 9);


    public final long modes;

    /**
     * Constructor is private to provide only existing modes that are functional
     * @param modes chosen modes
     */
    @Immutable
    private CallbackMode(long modes) {
        this.modes = modes;
    }

    /**
     * Adds callback modes to current
     * @param modes added modes
     * @return new callback mode
     */
    public CallbackMode add(CallbackMode modes) {
        return new CallbackMode(this.modes | modes.modes);
    }

    /**
     * Removes callback modes to current
     * @param modes removed modes
     * @return new callback mode
     */
    public CallbackMode remove(CallbackMode modes) {
        return new CallbackMode(this.modes ^ (modes.modes & this.modes));
    }

    /**
     * Checks if 'this' enable 'modes'
     * @param modes tested modes
     * @return true if 'this' enable all 'modes'
     */
    public boolean enable(CallbackMode modes) {
        return (this.modes | modes.modes) == this.modes;
    }
}
