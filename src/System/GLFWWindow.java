package System;

import Graphics.*;
import OpenGL.GLM;
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
import static org.lwjgl.system.MemoryUtil.memByteBufferNT1;
import static org.lwjgl.system.MemoryUtil.memPointerBuffer;

public class GLFWWindow extends RenderTarget {
    public static final ByteBuffer NO_ICON[] = new ByteBuffer[0];

    // window settings
    private int width;
    private int height;
    private boolean running = false;


    // window event
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

    private void initCallbacks() {
        glfwSetKeyCallback(this.glId, (window, key, scancode, action, mods) -> {
            switch (action) {
                case GLFW_PRESS: keyPressed = key; keyPressedEvent = true; break;
                case GLFW_RELEASE: keyReleased = key; keyReleasedEvent = true; break;
                default: break;
            }
        });
        glfwSetCharCallback(this.glId, (window, unicode) -> {
            textEntered = unicode;
            textEvent = true;
        });
        glfwSetCursorEnterCallback(this.glId, (window, state) -> {
            mouseCollisionEvent = true;
            mouseCollision = state;
        });
        glfwSetMouseButtonCallback(this.glId, (window, button, action, mods) -> {
            switch (action) {
                case GLFW_PRESS: buttonPressed = button; buttonPressedEvent = true; break;
                case GLFW_RELEASE: buttonReleased = button; buttonReleasedEvent = true; break;
                default: break;
            }
        });
        glfwSetScrollCallback(this.glId, (window, offsetx, offsety) -> {
            scrollEvent = true;
            scrollX = (float)offsetx;
            scrollY = (float)offsety;
        });
        glfwSetDropCallback(this.glId, (window,count, names) -> {
            dropEvent = true;
            drop = new String[count];

            PointerBuffer nameBuffer = memPointerBuffer(names, count);
            for (int i = 0; i < count; i++) {
                drop[i] = org.lwjgl.system.MemoryUtil.memASCII(nameBuffer.get(i));
            }
        });
        glfwSetJoystickCallback((joystick, event) -> {
            joystickEvent = true;
            this.joystick = joystick;
            joystickTriggeredEvent = event;
        });
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
        glfwSetWindowPosCallback(this.glId, (window, xpos, ypos) -> {
            moveEvent = true;
            posx = xpos;
            posy = ypos;
        });
        glfwSetWindowFocusCallback(this.glId, (window, focus)->{
            focusEvent = true;
            this.focus = focus;
        });
    }

    private void initHints(WindowStyle style) {
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, ((style.bits & WindowStyle.VISIBLE.bits) == WindowStyle.VISIBLE.bits) ? GLFW_TRUE : GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, ((style.bits & WindowStyle.RESIZABLE.bits) == WindowStyle.RESIZABLE.bits) ? GLFW_TRUE : GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_DECORATED, ((style.bits & WindowStyle.TITLEBAR.bits) == WindowStyle.TITLEBAR.bits) ? GLFW_TRUE : GLFW_FALSE); // the window will have title bar
        glfwWindowHint(GLFW_FLOATING, ((style.bits & WindowStyle.TOPMOST.bits) == WindowStyle.TOPMOST.bits) ? GLFW_TRUE : GLFW_FALSE);
        //glfwWindowHint(GLFW_STEREO, GLFW_TRUE);
    }

    private void initVideoMode() {

    }

    protected GLFWWindow(int width, int height, String title) {
        super();

        this.width = width;
        this.height = height;

        //////////////////////// Set up window //////////////////////////////
        // Configure our window
        this.initHints(WindowStyle.DEFAULT);

        // Create the window
        this.glId = glfwCreateWindow(this.width, this.height, title, 0, 0);
        if (this.glId == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.glId);

        // create a current thread context
        GL.createCapabilities();

        ////////////////////// Set up event handle //////////////////////////
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        this.initCallbacks();

        /////////////////////  Set up params ////////////////////
        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                this.glId,
                (vidmode.width() - this.width) / 2,
                (vidmode.height() - this.height) / 2
        );


        // Enable v-sync
        //glfwSwapInterval(1);
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.glId);

        this.running = true;

        this.initGl();

        Keyboard.setContext(this);
        Mouse.setContext(this);
    }

    protected GLFWWindow(int width, int height, String title, WindowStyle style) {
        super();

        this.width = width;
        this.height = height;

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
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        this.initCallbacks();

        /////////////////////  Set up params ////////////////////
        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                this.glId,
                (vidmode.width() - this.width) / 2,
                (vidmode.height() - this.height) / 2
        );


        // Enable v-sync
        //glfwSwapInterval(1);
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.glId);

        this.running = true;


        this.initGl();

        Keyboard.setContext(this);
        Mouse.setContext(this);
    }

    protected void initGl() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        Matrix4f ortho = GLM.ortho(0.f, this.getDimension().x, this.getDimension().y, 0.f, -1f, 1.f);
        glLoadMatrixf(GLM.toFloatArray(ortho));

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void close() {
        if (!running) return;

        running = false;

        glfwFreeCallbacks(this.glId);
        glfwDestroyWindow(this.glId);
        glfwTerminate();
    }

    public boolean isOpen() {
        return running;
    }

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
            return new Event(Event.Type.JOYSTICK);
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

    final public Event pollEvents() {
        if (!running) return null;

        glfwPollEvents();

        return pollEvent();
    }

    final public Event waitEvent() {
        if (!running) return null;

        glfwWaitEvents();

        return pollEvent();
    }

    public void clear(){
        if (!this.isOpen()) return ;

        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0,0,0,0);
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

    }

    public void setPosition(Vector2f position){
        glfwSetWindowPos(this.glId, (int)position.x, (int)position.y);
    }

    public void setDimension(Vector2f dimension){
        this.width = (int)dimension.x;
        this.height = (int)dimension.y;
        glfwSetWindowSize(this.glId, (int)dimension.x, (int)dimension.y);
    }

    public Vector2f getPosition() {
        return new Vector2f(0,0);
    }

    public Vector2f getDimension() {
        return new Vector2f(width, height);
    }

    public void hide() {
        glfwHideWindow(this.getGlId());
    }

    public void show() {
        glfwShowWindow(this.getGlId());
    }

    public static void main(String[] args) {
        GLFWWindow window = new GLFWWindow(2144, 1206, "OpenGL", WindowStyle.FULLSCREEN);

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

}
