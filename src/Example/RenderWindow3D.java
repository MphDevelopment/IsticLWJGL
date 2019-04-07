package Example;

import Graphics.Sprite;
import Graphics.Texture;
import Graphics.Color;
import Graphics.Shader;
import Graphics.VBO.BufferObject;
import Graphics.VBO.BumpVBO;
import Graphics.VBO.CubeVBO;
import Graphics.VBO.TexturedVBO;
import Graphics.Vector3f;
import OpenGL.GLM;
import System.*;
import System.CameraMode.Camera3DMode;
import System.CameraMode.FPSCamera3DMode;
import System.Event;
import System.Camera3D;
import org.lwjgl.util.vector.Matrix4f;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

/**http://schabby.de/opengl-shader-example/*/

public class RenderWindow3D extends GLFWWindow {
    private static final double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public RenderWindow3D(){
        super((int)width/3,(int)height/3, "3D Example Test");
    }

    //@Override
    protected void initGl() {
        glClearDepth(1.f);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDepthRange(0.f, 1.f);

        glMatrixMode( GL_PROJECTION );
        glLoadIdentity();
        //Matrix4f perspect = GLM.perspective(70, this.getDimension().x / this.getDimension().y, 1, 3000);
        //glLoadMatrixf(GLM.toFloatArray(perspect));

        glMatrixMode( GL_MODELVIEW );
        glLoadIdentity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void clear() {
        if (!this.isOpen()) return ;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void clear(Color color) {
        if (!this.isOpen()) return ;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(color.r, color.g, color.b, color.a);
    }

    public static void main(String[] args) {
        RenderWindow3D window = new RenderWindow3D();


        Mouse.setMouseCursorDisabled(window, false);
        Keyboard.setContext(window);


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
        //camera.setPosition(new Vector3f(0,0,-50));
        camera.apply(window);
        Clock clk = new Clock(Clock.Mode.NANOSECONDS_ACCURACY);


        //Camera3DMode mode2 = new SphereCamera3DMode(20.f);
        Camera3DMode mode = new FPSCamera3DMode();

        final int uniformMatrix = glGetUniformLocation((int)shader.getGlId(), "modelMatrix");
        final int uniformView = glGetUniformLocation((int)shader.getGlId(), "viewMatrix");
        final int uniformProjection = glGetUniformLocation((int)shader.getGlId(), "projectionMatrix");

        while (window.isOpen()) {
            Time elapsed = clk.restart();

            //System.out.println("fps:"+1.0/elapsed.asSeconds());
            //System.out.println("seconds:"+elapsed.asSeconds());

            Event event = null;
            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                }
            }

            if (window.isOpen()) {
                /*if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                    window.close();
                    break ;
                }*/

                mode.apply(camera, elapsed);

                camera.apply(window);

                window.clear();

                sprite.draw();

                glBegin(GL_TRIANGLES);
                glColor3d(1, 0, 0);
                glVertex3d(0, 0, 0);
                glVertex3f(0, 50, 0);
                glVertex3f(50, 50, 0);
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
