package System;

import Graphics.*;
import OpenGL.GLM;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.vector.Matrix4f;


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


    //Camera camera;
    //Viewport viewport;


    // window event values
    private int textEntered;
    private boolean textEvent = false;
    private int keyPressed;
    private int keyReleased;
    private boolean keyPressedEvent = false;
    private boolean keyReleasedEvent = false;
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
        if (modes.enable(CallbackMode.KEY))
            glfwSetKeyCallback(this.glId, (window, key, scancode, action, mods) -> {
            switch (action) {
                case GLFW_PRESS: keyPressed = key; keyPressedEvent = true; break;
                case GLFW_RELEASE: keyReleased = key; keyReleasedEvent = true; break;
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
        if (modes.enable(CallbackMode.RESIZE))
            glfwSetWindowSizeCallback(this.glId, (window, w, h) -> {
            resizeEvent = true;
            resizex = w;
            resizey = h;
            this.width = resizex;
            this.height = resizey;

            //glfwSetWindowSize(this.glId, this.width, this.height);
            glViewport(0,0, this.width, this.height);
            this.initGl();
        });
        if (modes.enable(CallbackMode.MOVE))
            glfwSetWindowPosCallback(this.glId, (window, xpos, ypos) -> {
            moveEvent = true;
            posx = xpos;
            posy = ypos;
        });
        if (modes.enable(CallbackMode.FOCUS))
            glfwSetWindowFocusCallback(this.glId, (window, focus)->{
            focusEvent = true;
            this.focus = focus;
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
     * Default initialization of window's MVP and Viewport and enabled gl states
     */
    //TODO on doit faire en sorte que les matrices se mettent a jour entre chaque different bind de RenderTarget
    protected void initGl() {
        //TODO RenderTargets must own their own view (Camera) and viewport (Viewport).

        glViewport(0, 0, width, height);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        Matrix4f ortho = GLM.ortho(0.f, this.getDimension().x, this.getDimension().y, 0.f, -1f, 1.f);
        glLoadMatrixf(GLM.toFloatArray(ortho));

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    //TODO RenderTargets must own their own view enabled gl states called each time the RenderTarget is bound
    private void initGlEnable() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }


    /**
     * Create a window with a title bar, title.
     * By default it enable all callbacks methods.
     * @param videoMode window's dimension
     * @param title  window's title
     */
    protected GLFWWindow(VideoMode videoMode, String title) {
        this(videoMode, title, WindowStyle.DEFAULT);
    }

    /**
     * Create a window with a specific style.
     * By default it enable all callbacks methods.
     * @param videoMode window's dimension
     * @param title window's title
     * @param style window's style
     */
    protected GLFWWindow(VideoMode videoMode, String title, WindowStyle style) {
        this(videoMode, title, style, CallbackMode.DEFAULT);
    }

    /**
     * Create a window with a specific style and specific callback modes.
     * @param videoMode window's dimension
     * @param title window's title
     * @param style window's style
     * @param modes window's callback modes
     */
    protected GLFWWindow(VideoMode videoMode, String title, WindowStyle style, CallbackMode modes) {
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
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(this.glId, (vidmode.width() - this.width) / 2, (vidmode.height() - this.height) / 2);


        // Enable v-sync
        glfwSwapInterval(1);
        //glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.glId);

        this.running = true;


        this.initGl();

        Keyboard.setContext(this);
        Mouse.setContext(this);
    }




    /**
     * Close and destroy the window.
     * @see GLFWWindow#isOpen return now false
     */
    public void close() {
        if (!running) return;

        this.free();
    }

    /**
     * Checks if the window is running
     * @return true if the window is running
     */
    public boolean isOpen() {
        return running;
    }

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
     */
    final public Event pollEvents() {
        if (!running) return null;

        glfwPollEvents();

        return pollEvent();
    }

    /**
     * Waits GLFW events then generate the event caught associated with its specific case values.
     * @return event caught by the window callbacks methods
     */
    final public Event waitEvent() {
        if (!running) return null;

        glfwWaitEvents();

        return pollEvent();
    }

    public void clear(){
        if (!this.isOpen()) return ;

        glClearColor(0,0,0,1);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void bind() {
        this.initGl();
    }

    @Override
    public void clear(Color color){
        if (!this.isOpen()) return ;

        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(color.r,color.g,color.b,color.a);
    }

    public void display() {
        if (!running) return ;

        //glFlush();
        glfwSwapBuffers(this.glId);
    }

    public void draw(Drawable drawable) {
        if (!running) return ;

        drawable.draw();
    }

    @Override
    public void free() {
        running = false;

        glfwFreeCallbacks(this.glId);
        glfwDestroyWindow(this.glId);

        // destroy glfw if glfw is initialized and there is no GLFWContext anymore
        GLFWContext.deleteContext();

        this.glId = 0;
    }

    public void setPosition(Vector2i position){
        glfwSetWindowPos(this.glId, position.x, position.y);
    }

    public void setDimension(VideoMode dimension){
        this.width = dimension.width;
        this.height = dimension.height;
        glfwSetWindowSize(this.glId, dimension.width, dimension.height);
    }

    public Vector2i getPosition() {
        return new Vector2i(0,0);
    }

    public Vector2i getDimension() {
        return new Vector2i(width, height);
    }

    public void hide() {
        glfwHideWindow(this.getGlId());
    }

    public void show() {
        glfwShowWindow(this.getGlId());
    }

    public static void main(String[] args) {
        GLFWWindow window = new GLFWWindow(VideoMode.getDesktopMode(), "OpenGL", WindowStyle.FULLSCREEN);

        //RenderTexture renderTexture = null;
        Texture texture = null;
        try {
            //renderTexture = new RenderTexture(100,100);
            texture = new Texture("dalle.png");
        } catch (IOException e) {
            window.close();
            return ;
        }

        texture.setWrapMode(Texture.REPEAT);

        RectangleShape shape = new RectangleShape(10,10, 10,10);
        shape.setFillColor(Color.Red);
        RectangleShape shape2 = new RectangleShape(10,100, 50,50);
        shape2.setFillColor(Color.Yellow);

        Sprite sprite = new Sprite();
        //sprite.setFillColor(Color.Yellow);
        sprite.setTexture(texture, true);
        sprite.setTextureRect(0,0,window.getDimension().x,window.getDimension().y);
        //sprite.setScale(0.5f,0.5f);
        sprite.setPosition(200,100);


        Keyboard keyboard = new Keyboard(window);

        while (window.isOpen()) {
            if (Keyboard.isKeyPressed_(GLFW_KEY_LEFT)) {
                sprite.move(-1f, 0);
            }
            if (Keyboard.isKeyPressed_(GLFW_KEY_RIGHT)) {
                sprite.move(1f, 0);
            }
            if (Keyboard.isKeyPressed_(GLFW_KEY_UP)) {
                sprite.move(0, -1f);
            }
            if (Keyboard.isKeyPressed_(GLFW_KEY_DOWN)) {
                sprite.move(0, 1f);
            }


            window.clear(Color.Magenta);

            //texture.bind();
            shape2.draw();

            sprite.draw();
            shape.draw();
            window.display();

            Event event;
            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                    System.exit(0);
                }
            }
        }
    }

    public Image capture() {
        final int bpp = 4;

        this.bind();

        glReadBuffer(GL_FRONT);
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);

        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return new Image(buffer, width, height);
    }

}


