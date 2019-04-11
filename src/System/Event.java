package System;

import Annotation.*;

/**
 * Interface used (by GLFWWindow) to describe a callback event and used to let the user known the description of the callback event outside callbacks.
 * Each parameter of Event corresponds to callbacks parameters. Each event must used it own values corresponding to its event type.
 * For example, when the event's type is 'TEXTENTERED', user must use the corresponding attribute 'textEntered'.
 * @see GLFWWindow#pollEvents()
 * @see GLFWWindow#waitEvent()
 */
public final class Event {
    public final Type type;

    // window event values
    // TODO instead of using multiple variable we must use C Union-like type to decrease memory use
    public int textEntered;
    public int keyPressed;
    public int keyReleased;
    public int buttonPressed;
    public int buttonReleased;
    public boolean mouseCollision;
    public float scrollX;
    public float scrollY;
    public String drop[];
    public int joystick;
    public int resizex;
    public int resizey;
    public int posx;
    public int posy;




    @OnlyGLFWWindowCallable
    public Event(Type type) {
        this.type = type;

        switch (type) {
            case MOUSEENTER: mouseCollision = true; break;
            case MOUSELEAVE: mouseCollision = false; break;
        }
    }

    @OnlyGLFWWindowCallable
    public Event(Type type, boolean[] values) {
        this(type);
    }

    @OnlyGLFWWindowCallable
    public Event(Type type, int[] values){
        this(type);

        switch (type) {
            case TEXTENTERED: textEntered = values[0]; break;

            case BUTTONPRESSED: buttonPressed = values[0]; break;
            case BUTTONRELEASED: buttonReleased = values[0]; break;
            case KEYPRESSED: keyPressed = values[0]; break;
            case KEYRELEASED: keyReleased = values[0]; break;

            case RESIZE: resizex = values[0]; resizey = values[1]; break;
            case MOVE: posx = values[0]; posy = values[1]; break;

            case JOYSTICK: joystick = values[1]; break;
        }
    }

    @OnlyGLFWWindowCallable
    public Event(Type type, float[] values){
        this(type);

        switch (type) {
            case MOUSESCROLL: scrollX = values[0]; scrollY = values[1]; break;
        }
    }

    @OnlyGLFWWindowCallable
    public Event(Type type, String[] values) {
        this(type);

        switch (type) {
            case MOUSEDROP: drop = values; break;
        }
    }

    public enum Type {
        CLOSE,
        RESIZE,
        FOCUS,
        UNFOCUS,
        MOVE,

        KEYPRESSED,
        KEYRELEASED,
        TEXTENTERED,

        BUTTONPRESSED,
        BUTTONRELEASED,

        MOUSEENTER,
        MOUSELEAVE,

        MOUSESCROLL,

        MOUSEDROP,

        JOYSTICK
    }
}
