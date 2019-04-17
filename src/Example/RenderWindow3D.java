package Example;

import Graphics.*;
import Graphics.VBO.BufferObject;
import Graphics.VBO.BumpVBO;
import Graphics.VBO.CubeVBO;
import Graphics.VBO.TexturedVBO;
import System.*;
import System.CameraMode.Camera3DMode;
import System.CameraMode.FPSCamera3DMode;
import System.Event;
import System.Camera3D;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

/**http://schabby.de/opengl-shader-example/*/

public final class RenderWindow3D extends GLFWWindow {

    public RenderWindow3D(){
        super(new VideoMode((int)(VideoMode.getDesktopMode().width/3.f), (int)(VideoMode.getDesktopMode().height/3.f)), "3D Example Test");
    }

    /**
     * Init Camera as Camera3D.
     * Init Viewport using default Window Dimension.
     */
    @Override
    protected void initGl() {
        //TODO RenderTargets must own their own view (Camera) and viewport (Viewport).
        defaultCamera = new Camera3D();
        camera = defaultCamera;
        defaultViewport = new Viewport(new FloatRect(0,0, super.getDimension().x, super.getDimension().y));
        viewport = defaultViewport;

        camera.apply();
        viewport.apply(this);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Update Default Camera (Camera3D) and Default Viewport.
     */
    @Override
    protected final void updateDefaultView(){
        ((Camera3D)defaultCamera).setAspectRatio(super.getDimension().x/super.getDimension().y);
        defaultViewport.setDimension(new Vector2f(super.getDimension()));
    }







    public static void main(String[] args) {
        RenderWindow3D window = new RenderWindow3D();




        Shader shader = null;
        Shader tshader = null;
        Shader bshader = null;
        Texture texture = null;
        Texture benjibob = null;
        Texture bumpTexture = null;
        Texture transparent = null;

        try {
            shader = new Shader("shaders/model.vert", "shaders/mvp.frag");
            tshader = new Shader("shaders/defaultMVP.vert", "shaders/defaultMVP.frag");
            bshader = new Shader("shaders/bumpMapping.vert", "shaders/bumpMapping.frag");
            texture = new Texture("dalle.png");
            benjibob = new Texture("ben10.png");
            bumpTexture = new Texture("bump.png");
            transparent = new Texture("transp.png");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        texture.setWrapMode(Texture.REPEAT);
        benjibob.setWrapMode(Texture.MIRROR);

        Sprite sprite = new Sprite();
        //sprite.setFillColor(Color.Blue);
        sprite.setTexture(texture, true);
        sprite.setTextureRect(0,0,window.getDimension().x,window.getDimension().y);
        sprite.setScale(-1.f,- 1.f);

        TexturedVBO vbot = new TexturedVBO(tshader, BufferObject.BindMode.STATIC_DRAW, new Vector3f(0,0,100), 100, benjibob);

        final float length = 20;
        BufferObject cube = new CubeVBO(shader, new Vector3f(0,0,0));
        BufferObject cube2 = new CubeVBO(shader, new Vector3f(30,-25,50));
        BufferObject cube3 = new TexturedVBO(tshader, BufferObject.BindMode.STATIC_DRAW, new Vector3f(0,0,0), length, benjibob);
        BufferObject cube4 = new TexturedVBO(tshader, BufferObject.BindMode.STATIC_DRAW, new Vector3f(length,0,0), length, benjibob);
        BufferObject cube5 = new TexturedVBO(tshader, BufferObject.BindMode.STATIC_DRAW, new Vector3f(0,length,0), length, benjibob);
        BufferObject cube6 = new TexturedVBO(tshader, BufferObject.BindMode.STATIC_DRAW, new Vector3f(0,0,length), length, benjibob);
        ArrayList<BufferObject> cubes = new ArrayList<>();
        for (int i=0 ; i < 3000 ; ++i) {
            cubes.add(new CubeVBO(shader, BufferObject.BindMode.STATIC_DRAW, new Vector3f(i/100 * 20,i%100 * 20,10), 10));
        }
        BufferObject bump = new BumpVBO(bshader, BufferObject.BindMode.STATIC_DRAW, new Vector3f(-5*length,length,length), length, transparent, bumpTexture);


        Camera3D camera = new Camera3D(80.f, 1,1000, window);
        camera.setUpVector(new Vector3f(0,1,0));

        camera.apply();
        Clock clk = new Clock(Clock.Mode.NANOSECONDS_ACCURACY);


        //Camera3DMode mode2 = new SphereCamera3DMode(20.f);
        Keyboard keyboard = new Keyboard(window, Keyboard.AZERTY);
        Camera3DMode mode = new FPSCamera3DMode(keyboard);

        final int uniformMatrix = glGetUniformLocation((int)shader.getGlId(), "modelMatrix");
        final int uniformView = glGetUniformLocation((int)shader.getGlId(), "viewMatrix");
        final int uniformProjection = glGetUniformLocation((int)shader.getGlId(), "projectionMatrix");

        Viewport viewport = new Viewport(new FloatRect(50,50, 800,800));

        Time elapsedSinceBeginning = Time.zero();
        while (window.isOpen()) {
            Time elapsed = clk.restart();
            elapsedSinceBeginning.add(elapsed);

            //System.out.println("fps:"+1.0/elapsed.asSeconds());
            //System.out.println("seconds:"+elapsed.asSeconds());

            Event event;
            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                    System.exit(0);
                } else if (event.type == Event.Type.RESIZE) {
                    camera.setAspectRatio((float)event.resizeX /(float)event.resizeY);
                } else if (event.type == Event.Type.KEYRELEASED && event.keyReleased == GLFW_KEY_P) {
                    System.out.println("Screenshot! 'capture.png'");
                    try {
                        window.capture().saveAs("capture");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.type == Event.Type.KEYPRESSED) {
                    //System.out.println(event.keyPressed);
                } else if (event.type == Event.Type.TEXTENTERED) {
                    System.out.println((char)event.textEntered);
                }
            }

            if (window.isOpen()) {
                /*for (int key=32; key <= GLFW_KEY_LAST ; ++key) {
                    if (keyboard.isKeyPressed(key)) {
                        System.out.println(glfwGetKeyName(key, 0));
                        break;
                    }
                }*/

                mode.apply(camera, elapsed);

                //viewport update
                //viewport.setTopleftCorner(new Vector2f((float)elapsedSinceBeginning.asMilliseconds()/100, (float)elapsedSinceBeginning.asMilliseconds()/100));
                camera.setAspectRatio(viewport.getDimension().x/viewport.getDimension().y);
                camera.apply();
                viewport.apply(window);

                window.clear(new Color(0.1f,0.1f,0.1f));

                sprite.draw();
                glBegin(GL_TRIANGLES);
                glColor3d(1, 0, 0);
                glVertex3d(0+(float)elapsedSinceBeginning.asMilliseconds()/10, 0, 0);
                glVertex3f(0+(float)elapsedSinceBeginning.asMilliseconds()/10, 50, 0);
                glVertex3f(50+(float)elapsedSinceBeginning.asMilliseconds()/10, 50, 0);
                glColor3d(0,1,0);
                glVertex3d(50, 50, 50);
                glVertex3f(0, -50, 50);
                glVertex3f(50, 50, 0);
                glEnd();

                shader.bind();
                camera.glUniformMVP(uniformMatrix, uniformView, uniformProjection);
                cube.draw();
                cube2.draw();
                for (int i=0 ; i < cubes.size() ; ++i) {
                    cubes.get(i).draw();
                }

                ///CAUSE COLOR ERROR FOR DL DRAW
                /*bshader.bind();
                camera.glUniformMVP(uniformMatrix, uniformView, uniformProjection);
                bump.draw();*/

                tshader.bind();
                camera.glUniformMVP(uniformMatrix, uniformView, uniformProjection);
                cube3.draw();
                cube4.draw();
                cube5.draw();
                cube6.draw();
                vbot.draw();
                Shader.unbind();

                window.display();
            }
        }
    }

}
