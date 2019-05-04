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
            shader = new Shader("shaders/model.vert", "shaders/vao/textured/mvp.frag");
            tshader = new Shader("shaders/defaultMVP.vert", "shaders/defaultMVP.frag");
            bshader = new Shader("shaders/bumpMapping.vert", "shaders/bumpMapping.frag");
            texture = new Texture("dalle.png");
            benjibob = new Texture("Phases.bmp");
            bumpTexture = new Texture("bump.png");
            transparent = new Texture("transp.png");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        texture.setWrapMode(Texture.REPEAT);
        benjibob.setWrapMode(Texture.MIRROR);

        RectangleShape shape = new RectangleShape(-100, -100, 1000,1000);
        shape.setFillColor(Color.Red);

        Sprite sprite = new Sprite(/*texture*/);
        sprite.setFillColor(Color.Blue);
        sprite.setTexture(texture, true);
        sprite.setTextureRect(0,0,window.getDimension().x,window.getDimension().y);
        sprite.setOrigin(-window.getDimension().x/2.f,-window.getDimension().y/2.f);
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

        VertexBuffer VERTEX = new VertexBuffer(6, 2, new int[]{3,4}, VertexBuffer.Mode.TRIANGLES, VertexBuffer.Usage.STREAM);
        VERTEX.update(0, new float[]{
                0, 0, 0,
                1000, 0, 0,
                1000,1000,0,

                0,0,0,
                1000,1000,0,
                0,1000,0
        });
        VERTEX.update(1, new float[]{
                1, 1, 0, 1,
                1, 0, 0, 1,
                1, 1, 0, 1,

                1, 0, 1, 1,
                0, 0, 0, 1,
                1, 0, 1, 1
        });

        ArrayList<Drawable> vertices = new ArrayList<>();
        for (int i = 0 ; i < 100 ; ++i) {
            VertexBuffer tmp = new VertexBuffer(6, 3, new int[]{3,4,2}, VertexBuffer.Mode.TRIANGLES, VertexBuffer.Usage.STREAM);
            tmp.update(0, new float[]{
                    0, 0+ i%10 * 100, 0 + i/100 * 100.f,
                    0, 0 + i%10 * 100, 100 + i/100 * 100.f,
                    0,100+ i%10 * 100,100 + i/100 * 100.f,

                    0,0+ i%10 * 100,0 + i/100 * 100.f ,
                    0,100+ i%10 * 100,100 + i/100* 100.f ,
                    0,100+ i%10 * 100,0 + i/100* 100.f
            });
            tmp.update(1, new float[]{
                    0, 0, 0, 1,
                    1, 1, 1, 1,
                    1, 1, 1, 1,

                    0, 0, 0, 1,
                    1, 1, 1, 1,
                    1, 1, 1, 1,
            });
            tmp.update(2, new float[]{
                    1, 1,
                    1, 0,
                    0, 0,

                    1, 1,
                    0, 0,
                    0, 1
            });
            vertices.add(tmp);
        }
        VertexBuffer VERTEX2 = new VertexBuffer(6, 3, new int[]{3,4,2}, VertexBuffer.Mode.TRIANGLES, VertexBuffer.Usage.STREAM);
        VERTEX2.update(0, new float[]{
                0, 0, 0,
                0, 0, 1000,
                0,1000,1000,

                0,0,0,
                0,1000,1000,
                0,1000,0
        });
        VERTEX2.update(1, new float[]{
                0, 0, 0, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,

                0, 0, 0, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
        });
        VERTEX2.update(2, new float[]{
                1, 1,
                1, 0,
                0, 0,

                1, 1,
                0, 0,
                0, 1
        });




        Camera3D camera = new Camera3D(80.f, 1,1000, window);

        camera.apply();
        Clock clk = new Clock(Clock.Mode.NANOSECONDS_ACCURACY);


        //Camera3DMode mode2 = new SphereCamera3DMode(20.f);
        Keyboard keyboard = new Keyboard(window, Keyboard.AZERTY);
        Camera3DMode mode = new FPSCamera3DMode(keyboard);

        final int uniformMatrix = shader.getUniformLocation("modelMatrix");
        final int uniformView = shader.getUniformLocation("viewMatrix");
        final int uniformProjection = shader.getUniformLocation("projectionMatrix");

        Viewport viewport = new Viewport(new FloatRect(50,50, 800,800));

        window.setCamera(camera);
        window.setViewport(viewport);

        Time elapsedSinceBeginning = Time.zero();
        while (window.isOpen()) {
            Time elapsed = clk.restart();
            elapsedSinceBeginning.add(elapsed);


            float sec = (float)elapsedSinceBeginning.asSeconds();
            VERTEX2.update(0, new float[]{
                    sec*100, 0, 0,
                    sec*100, 0, 1000,
                    sec*100,1000,1000,

                    sec*100,0,0,
                    sec*100,1000,1000,
                    sec*100,1000,0
            });

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
                    System.out.println("Screenshot! 'capture.*'");
                    try {
                        window.capture().saveAs("capture.PNG");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.type == Event.Type.KEYRELEASED) {
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


                sprite.rotate((float)(elapsed.asSeconds()/5));

                window.clear(new Color(0.1f,0.1f,0.1f));

                Texture.unbind();
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
                camera.setUniformMVP(uniformMatrix, uniformView, uniformProjection);
                shape.draw();
                VERTEX.draw();
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
                camera.setUniformMVP(uniformMatrix, uniformView, uniformProjection);
                sprite.draw();
                texture.bind();
                for (int i=0 ; i < vertices.size() ; ++i) {
                    vertices.get(i).draw();
                }
                VERTEX2.draw();
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
