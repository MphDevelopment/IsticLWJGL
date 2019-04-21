package System;

import Graphics.*;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL;


import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.system.MemoryUtil.memPointerBuffer;

/**
 * Interface to manage a Graphics Window.
 */
public class GLFWWindow extends RenderTarget {
    // window settings
    private int width;
    private int height;
    private boolean running = false;

    // window event values
    private int textEntered;
    private boolean textEvent = false;
    private int keyPressed;
    private int keyReleased;
    private int keyRepeated;
    private boolean keyPressedEvent = false;
    private boolean keyReleasedEvent = false;
    private boolean keyRepeatEvent = false;
    private int buttonPressed;
    private int buttonReleased;
    private boolean buttonPressedEvent = false;
    private boolean buttonReleasedEvent = false;
    private boolean mouseCollision = false;
    private boolean mouseCollisionEvent = false;
    private boolean scrollEvent = false;
    private float scrollX;
    private float scrollY;
    private boolean dropEvent = false;
    private String drop[];
    private boolean joystickEvent = false;
    private int joystick;
    private int joystickTriggeredEvent;
    private boolean resizeEvent = false;
    private int resizex;
    private int resizey;
    private boolean moveEvent = false;
    private int posx;
    private int posy;
    private boolean focusEvent = false;
    private boolean focus;

    /**
     * Enable callbacks according to 'modes' parameter
     * @param modes chosen modes
     */
    private void initCallbacks(CallbackMode modes) {
        //Callbacks Inputs
        if (modes.enable(CallbackMode.KEY))
            glfwSetKeyCallback(this.glId, (window, key, scancode, action, mods) -> {
            switch (action) {
                case GLFW_PRESS: keyPressed = key; keyPressedEvent = true; break;
                case GLFW_RELEASE: keyReleased = key; keyReleasedEvent = true; break;
                case GLFW_REPEAT: keyRepeated = key; keyRepeatEvent = true; break;
                default: break;
            }
        });
        if (modes.enable(CallbackMode.CHAR))
            glfwSetCharCallback(this.glId, (window, unicode) -> {
            textEntered = unicode;
            textEvent = true;
        });
        if (modes.enable(CallbackMode.CURSORENTER))
            glfwSetCursorEnterCallback(this.glId, (window, state) -> {
            mouseCollisionEvent = true;
            mouseCollision = state;
        });
        if (modes.enable(CallbackMode.BUTTON))
            glfwSetMouseButtonCallback(this.glId, (window, button, action, mods) -> {
            switch (action) {
                case GLFW_PRESS: buttonPressed = button; buttonPressedEvent = true; break;
                case GLFW_RELEASE: buttonReleased = button; buttonReleasedEvent = true; break;
                default: break;
            }
        });
        if (modes.enable(CallbackMode.SCROLL))
            glfwSetScrollCallback(this.glId, (window, offsetx, offsety) -> {
            scrollEvent = true;
            scrollX = (float)offsetx;
            scrollY = (float)offsety;
        });
        if (modes.enable(CallbackMode.MOUSEDROP))
            glfwSetDropCallback(this.glId, (window,count, names) -> {
            dropEvent = true;
            drop = new String[count];

            PointerBuffer nameBuffer = memPointerBuffer(names, count);
            for (int i = 0; i < count; i++) {
                drop[i] = org.lwjgl.system.MemoryUtil.memASCII(nameBuffer.get(i));
            }
        });
        if (modes.enable(CallbackMode.JOYSTICK))
            glfwSetJoystickCallback((joystick, event) -> {
            joystickEvent = true;
            this.joystick = joystick;
            joystickTriggeredEvent = event;
        });
        if (modes.enable(CallbackMode.FOCUS))
            glfwSetWindowFocusCallback(this.glId, (window, focus)->{
                focusEvent = true;
                this.focus = focus;
        });

        // Callback window
        if (modes.enable(CallbackMode.RESIZE))
            glfwSetWindowSizeCallback(this.glId, (window, w, h) -> {
            resizeEvent = true;
            resizex = w;
            resizey = h;
            this.width = resizex;
            this.height = resizey;

            this.updateDefaultView();
        });
        if (modes.enable(CallbackMode.MOVE))
            glfwSetWindowPosCallback(this.glId, (window, xpos, ypos) -> {
            moveEvent = true;
            posx = xpos;
            posy = ypos;
        });


    }

    /**
     * Enable styles according to 'styles' parameter
     * @param styles chosen styles
     */
    private void initHints(WindowStyle styles) {
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, ((styles.bits & WindowStyle.VISIBLE.bits) == WindowStyle.VISIBLE.bits) ? GLFW_TRUE : GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, ((styles.bits & WindowStyle.RESIZABLE.bits) == WindowStyle.RESIZABLE.bits) ? GLFW_TRUE : GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_DECORATED, ((styles.bits & WindowStyle.TITLEBAR.bits) == WindowStyle.TITLEBAR.bits) ? GLFW_TRUE : GLFW_FALSE); // the window will have title bar
        glfwWindowHint(GLFW_FLOATING, ((styles.bits & WindowStyle.TOPMOST.bits) == WindowStyle.TOPMOST.bits) ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, ((styles.bits & WindowStyle.MAXIMIZED.bits) == WindowStyle.MAXIMIZED.bits) ? GLFW_TRUE : GLFW_FALSE);

        //glfwWindowHint(GLFW_STEREO, GLFW_TRUE);
    }

    /**
     * Default initialization of window's MVP and Viewport and enabled gl states.
     * Init Camera (default Camera2D).
     * Init Viewport (default Window dimension).
     */
    protected void initGl() {
        //TODO RenderTargets must own their own view (Camera) and viewport (Viewport).
        defaultCamera = new Camera2D(new Vector2f(width, height));
        camera = defaultCamera;
        defaultViewport = new Viewport(new FloatRect(0,0, width, height));
        viewport = defaultViewport;

        camera.apply();
        viewport.apply(this);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Updates current context Default Camera (default Camera2D) and Default Viewport.
     * If defaultCamera is not camera, camera must be updated outside.
     * If defaultViewport is not viewport, viewport must be updated outside.
     */
    protected void updateDefaultView(){
        ((Camera2D)defaultCamera).setDimension(new Vector2f(width, height));
        defaultViewport.setDimension(new Vector2f(width, height));
    }

    /**
     * Make 'this' as the current RenderTarget.
     */
    @Override
    protected final void bind(){
        glfwMakeContextCurrent(glId);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        camera.apply();
        viewport.apply(this);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }


    /**
     * Create a window with a title bar, title.
     * By default it enable all callbacks methods.
     * @param videoMode window's dimension
     * @param title  window's title
     */
    public GLFWWindow(VideoMode videoMode, String title) {
        this(videoMode, title, WindowStyle.DEFAULT);
    }

    /**
     * Create a window with a specific style.
     * By default it enable all callbacks methods.
     * @param videoMode window's dimension
     * @param title window's title
     * @param style window's style
     */
    public GLFWWindow(VideoMode videoMode, String title, WindowStyle style) {
        this(videoMode, title, style, CallbackMode.DEFAULT);
    }

    /**
     * Create a window with a specific style and specific callback modes.
     * @param videoMode window's dimension
     * @param title window's title
     * @param style window's style
     * @param modes window's callback modes
     */
    public GLFWWindow(VideoMode videoMode, String title, WindowStyle style, CallbackMode modes) {
        super();

        // initialized glfw if glfw is not initialized
        GLFWContext.createContext();

        this.width = videoMode.width;
        this.height = videoMode.height;

        //////////////////////// Set up window //////////////////////////////
        // Configure our window
        this.initHints(style);

        // Create the window
        this.glId = glfwCreateWindow(this.width, this.height, title, ((WindowStyle.FULLSCREEN.bits & style.bits) == WindowStyle.FULLSCREEN.bits) ? glfwGetPrimaryMonitor() : 0, 0);
        if (this.glId == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.glId);

        // create a current thread context
        GL.createCapabilities();


        ////////////////////// Set up event handle //////////////////////////
        // Setup a key callback.
        this.initCallbacks(modes);

        /////////////////////  Set up params ////////////////////
        // Get the resolution of the primary monitor
        VideoMode videomode = VideoMode.getDesktopMode();
        // Center our window
        posx = (videomode.width - this.width) / 2;
        posy = (videomode.height - this.height) / 2;
        glfwSetWindowPos(this.glId, posx, posy);


        // Enable v-sync
        glfwSwapInterval(1);
        //glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.glId);

        this.running = true;

        this.initGl();
    }


    /**
     * Close and destroy the window.
     * @see GLFWWindow#isOpen return now false
     */
    public final void close() {
        if (!running) return;

        this.free();
    }

    /**
     * Checks if the window is running
     * @return true if the window is running
     * @see GLFWWindow#close
     */
    public final boolean isOpen() {
        return running;
    }

    ///EVENTS
    /**
     * Generates an event associated with its specific case values.
     * @return event caught by the window callbacks methods
     */
    private Event pollEvent() {
        if (glfwWindowShouldClose(this.getGlId())) {
            return new Event(Event.Type.CLOSE);
        }
        if (keyPressedEvent) {
            keyPressedEvent = false;
            return new Event(Event.Type.KEYPRESSED, new int[]{keyPressed});
        }
        if (keyReleasedEvent) {
            keyReleasedEvent = false;
            return new Event(Event.Type.KEYRELEASED, new int[]{keyReleased});
        }
        if (textEvent) {
            textEvent = false;
            return new Event(Event.Type.TEXTENTERED, new int[]{textEntered});
        }
        if (mouseCollisionEvent) {
            mouseCollisionEvent = false;
            return new Event((mouseCollision) ? Event.Type.MOUSEENTER : Event.Type.MOUSELEAVE);
        }
        if (buttonPressedEvent) {
            buttonPressedEvent = false;
            return new Event(Event.Type.BUTTONPRESSED, new int[]{buttonPressed});
        }
        if (buttonReleasedEvent) {
            buttonReleasedEvent = false;
            return new Event(Event.Type.BUTTONRELEASED, new int[]{buttonReleased});
        }
        if (keyRepeatEvent) {
            keyRepeatEvent = false;
            return new Event(Event.Type.KEYREPEAT, new int[]{keyRepeated});
        }
        if (scrollEvent) {
            scrollEvent = false;
            return new Event(Event.Type.MOUSESCROLL, new float[]{scrollX, scrollY});
        }
        if (dropEvent) {
            dropEvent = false;
            return new Event(Event.Type.MOUSEDROP, drop);
        }
        if (joystickEvent) {
            joystickEvent = false;
            return new Event(Event.Type.JOYSTICK, new int[]{joystick, joystickTriggeredEvent});
        }
        if (resizeEvent) {
            resizeEvent = false;
            return new Event(Event.Type.RESIZE, new int[]{resizex, resizey});
        }
        if (moveEvent) {
            moveEvent = false;
            return new Event(Event.Type.MOVE, new int[]{posx, posy});
        }
        if (focusEvent) {
            focusEvent = false;
            return new Event(focus ? (Event.Type.FOCUS) : (Event.Type.UNFOCUS));
        }
        return null;
    }

    /**
     * Poll GLFW events and generate the last event caught associated with its specific case values.
     * @return event caught by the window callbacks methods
     * @see GLFWWindow#waitEvent()
     * @see GLFWWindow#waitEvent(Time)
     */
    public final Event pollEvents() {
        if (!running) return null;

        if (!this.isActive()) this.setActive();

        glfwPollEvents();

        return pollEvent();
    }

    /**
     * Waits GLFW events then generate the event caught associated with its specific case values.
     * @return event caught by the window callbacks methods
     * @see GLFWWindow#waitEvent(Time)
     * @see GLFWWindow#pollEvents()
     */
    public final Event waitEvent() {
        if (!running) return null;

        if (!this.isActive()) this.setActive();

        glfwWaitEvents();

        return pollEvent();
    }

    /**
     * Waits GLFW events until timeout then generate the event caught associated with its specific case values.
     * @param timeout time until the poll will stop
     * @return event caught by the window callbacks methods
     * @see GLFWWindow#waitEvent()
     * @see GLFWWindow#pollEvents()
     */
    public final Event waitEvent(Time timeout) {
        if (!running) return null;

        if (!this.isActive()) this.setActive();

        glfwWaitEventsTimeout(timeout.asSeconds());

        return pollEvents();
    }

    ///DISPLAY
    /**
     * Clear all the window screen with black color.
     */
    public final void clear(){
        if (!this.isOpen()) return ;

        if (!this.isActive()) this.setActive();
        else if (this.needViewUpdate()) this.applyView();

        glClearColor(0,0,0,1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Clear all the window screen with a specific color.
     * @param color specified color
     */
    @Override
    public final void clear(Color color){
        if (!this.isOpen()) return ;

        if (!this.isActive()) this.setActive();
        else if (this.needViewUpdate()) this.applyView();

        glClearColor(color.r,color.g,color.b,color.a);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Render frame buffer to screen.
     */
    public final void display() {
        if (!running) return ;

        if (!this.isActive()) this.setActive();
        else if (this.needViewUpdate()) this.applyView();

        //glFlush();
        glfwSwapBuffers(this.glId);
    }

    /**
     * Draw the Drawable inside the frame buffer.
     * @param drawable something that can be drawn
     */
    public final void draw(Drawable drawable) {
        if (!running) return ;

        if (!this.isActive()) this.setActive();
        else if (this.needViewUpdate()) this.applyView();

        drawable.draw();
    }


    @Override
    public final void free() {
        if (!running) return;

        running = false;

        glfwFreeCallbacks(this.glId);
        glfwDestroyWindow(this.glId);

        // destroy glfw if glfw is initialized and there is no GLFWContext anymore
        GLFWContext.deleteContext();

        this.glId = 0;
    }


    /**
     * Changes window's position into Desktop.
     * @param position desktop topleft position
     */
    public final void setPosition(Vector2i position){
        glfwSetWindowPos(this.glId, position.x, position.y);
    }

    /**
     * Changes window's dimension
     * @param dimension specified dimension
     */
    public final void setDimension(VideoMode dimension){
        this.width = dimension.width;
        this.height = dimension.height;
        glfwSetWindowSize(this.glId, dimension.width, dimension.height);

        this.updateDefaultView();
    }

    /**
     * Position of the window into Desktop.
     * @return window's position
     */
    public final Vector2i getPosition() {
        return new Vector2i(posx, posy);
    }

    /**
     * Dimension of the window;
     * @return window's dimension
     */
    public final Vector2i getDimension() {
        return new Vector2i(width, height);
    }

    public final void hide() {
        glfwHideWindow(this.getGlId());
    }

    public final void show() {
        glfwShowWindow(this.getGlId());
    }

    /**
     * Screenshot the GLFWWindow and gives an image of it.
     * @return screenshot image
     */
    @Override
    public final Image capture() {
        final int bpp = 4;

        if (!this.isActive()) this.setActive();
        else if (this.needViewUpdate()) this.applyView();

        glReadBuffer(GL_FRONT);
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * bpp);

        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        byte[] array = new byte[width*height*bpp];
        buffer.get(array);

        //invert y
        for (int i=0;i < width ; ++i) {
            for (int j=0; j < height/2 ; ++j) {
                byte[] rgba = new byte[]{
                        array[i * bpp + j * width * bpp    ],
                        array[i * bpp + j * width * bpp + 1],
                        array[i * bpp + j * width * bpp + 2],
                        array[i * bpp + j * width * bpp + 3]
                };

                array[i * bpp + j * width * bpp    ] = array[i * bpp + (height - j - 1) * width * bpp    ];
                array[i * bpp + j * width * bpp + 1] = array[i * bpp + (height - j - 1) * width * bpp + 1];
                array[i * bpp + j * width * bpp + 2] = array[i * bpp + (height - j - 1) * width * bpp + 2];
                array[i * bpp + j * width * bpp + 3] = array[i * bpp + (height - j - 1) * width * bpp + 3];

                array[i * bpp + (height - j - 1) * width * bpp    ] = rgba[0];
                array[i * bpp + (height - j - 1) * width * bpp + 1] = rgba[1];
                array[i * bpp + (height - j - 1) * width * bpp + 2] = rgba[2];
                array[i * bpp + (height - j - 1) * width * bpp + 3] = rgba[3];
            }
        }

        return new Image(array, width, height);
    }


    public static void main(String[] args) {
        GLFWWindow window = new GLFWWindow(/*VideoMode.getDesktopMode()*/new VideoMode(500,500), "OpenGL", WindowStyle.DEFAULT, CallbackMode.DEFAULT/*.remove(WindowStyle.RESIZABLE)*/);

        RectangleShape shape = new RectangleShape(10,10, 10,10);
        shape.setFillColor(Color.Red);
        RectangleShape shape2 = new RectangleShape(10,100, 50,50);
        shape2.setFillColor(Color.Yellow);

        Keyboard keyboard = new Keyboard(window, Keyboard.AZERTY);

        //Camera is not default window camera so it need to be updated when window is resized
        Camera2D tracker = new Camera2D(window);
        window.setCamera(tracker);


        while (window.isOpen()) {
            if (keyboard.isKeyPressed(GLFW_KEY_LEFT)) {
                shape.move(-1f, 0);
            }
            if (keyboard.isKeyPressed(GLFW_KEY_RIGHT)) {
                shape.move(1f, 0);
            }
            if (keyboard.isKeyPressed(GLFW_KEY_UP)) {
                shape.move(0, -1f);
            }
            if (keyboard.isKeyPressed(GLFW_KEY_DOWN)) {
                shape.move(0, 1f);
            }

            tracker.setCenter(shape.getPosition());

            window.clear();
            window.draw(shape2);
            window.draw(shape);
            window.display();


            Event event;
            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                    System.exit(0);
                }
                if (event.type == Event.Type.KEYRELEASED && event.keyReleased == GLFW_KEY_P) {
                    System.out.println("Screenshot! 'capture.png'");
                    try {
                        window.capture().saveAs("capture");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //Camera is not default window camera so it needs to be updated when window is resized
                if (event.type == Event.Type.RESIZE) {
                    tracker.setDimension(new Vector2f(event.resizeX, event.resizeY));
                }
            }
        }
    }

}


