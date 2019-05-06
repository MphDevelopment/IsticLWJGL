package Example;

import System.*;

import Graphics.*;
import Graphics.Color;
import System.IO.AZERTYLayout;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.opengl.GL11.*;

//TODO trouver un moyen de ne transmettre la MVP qu'une seule fois (uniform blocks, uniform buffer object)
public class RenderWindow {

    //TODO trouver un moyen de n'utiliser qu'un seul shaders en mettant par défaut la couleur blanche si pas de texture pour laisser apparaitre la couleur (super efficace)
    //TODO trouver un moyen de n'utiliser qu'un seul shaders en mettant par défaut une texture blanche 1*1 (assez efficace)
    //TODO ou utiliser deux shaders (peu efficace)

    public static void main(String[] args) {

        GLFWWindow window = new GLFWWindow(
                new VideoMode(900, 900), "OpenGL",
                WindowStyle.DEFAULT.add(WindowStyle.TOPMOST),
                CallbackMode.DEFAULT
        );
        //window.setFrameRateLimit(60);

        RenderTexture renderTexture;
        RenderTexture renderTexture2;

        Texture texture;
        FontFamily font;

        Shader texturedShader;
        Shader untexturedShader;
        try {
            texturedShader = new Shader("shaders/vao/textured/mvp.vert", "shaders/vao/textured/mvp.frag");
            untexturedShader = new Shader("shaders/vao/untextured/mvp.vert", "shaders/vao/untextured/mvp.frag");

            renderTexture = new RenderTexture(200,200);
            renderTexture2 = new RenderTexture(200,200);

            texture = new Texture("dalle.png");
            texture.setWrapMode(Texture.REPEAT);

            font = new FontFamily("default.ttf", 30);
            //font = new FontFamily("asmelina.ttf", 24);
            //font = new FontFamily("arial.ttf", 24);
            //font = new FontFamily("mono.ttf", 24);
        } catch (IOException e) {
            window.close();
            e.printStackTrace();
            return ;
        }

        Text text = new Text(font, "Phrase Italique!", Text.ITALIC);
        Text text2 = new Text(font, "Phrase Normale!", Text.REGULAR);
        Text text3 = new Text(font, "Phrase Grasse!", Text.BOLD);
        Text fpsText = new Text(font, "0", Text.BOLD);
        fpsText.setFillColor(Color.Red);

        text.setFillColor(Color.Black);
        text2.setFillColor(new Color(1, 0.59f, 0.2f));
        text3.setFillColor(new Color(0.59f, 1f, 0.5f));
        //text2.setScale(0.2f,0.5f);
        RectangleShape textShape = new RectangleShape(text2.getBounds().l, text2.getBounds().t, text2.getBounds().w, text2.getBounds().h);
        textShape.setFillColor(Color.Red);



        RectangleShape shape = new RectangleShape(10,10, 10,10);
        shape.setOrigin(5,5);
        shape.setFillColor(Color.Red);

        Sprite sprite = new Sprite(texture);
        sprite.move(50, 50);

        Sprite screen = new Sprite(renderTexture.getTexture());
        screen.setTextureRect(0,0,400,400);
        screen.move(50,50);
        screen.setScale(0.5f,-0.5f);

        Sprite screen2 = new Sprite(renderTexture2.getTexture());
        screen2.setTextureRect(0,0,200,200);
        screen.move(30,300);

        Keyboard keyboard = new Keyboard(window);

        Camera2D camera = new Camera2D(window);
        Camera2D screenCamera = new Camera2D(renderTexture);
        Camera2D screenCamera2 = new Camera2D(renderTexture2);
        window.setCamera(camera);
        renderTexture.setCamera(screenCamera);
        renderTexture2.setCamera(screenCamera2);

        RectangleShape background = new RectangleShape(200,200);
        background.setFillColor(Color.Blue);

        RectangleShape fullBackground = new RectangleShape(900,900);
        fullBackground.move(-300,-300);
        fullBackground.setFillColor(Color.Cyan);


        ArrayList<Shape> array = new ArrayList<>();
        final int arraySize = 3000;
        for (int i = 0; i < arraySize ; ++i) {
            Sprite tmp = new Sprite(texture);
            //RectangleShape tmp = new RectangleShape(1000,100);
            tmp.move((i%(int)Math.sqrt(arraySize))*100.f,(i/(int)Math.sqrt(arraySize))*100.f);
            tmp.setFillColor(new Color(i / (float)arraySize, i / (float)arraySize, i / (float)arraySize));
            tmp.setScale(0.9f, 0.9f);
            array.add(tmp);
        }

        Clock clk = new Clock(Clock.Mode.NANOSECONDS_ACCURACY);

        Viewport viewport = new Viewport(new FloatRect(0,0, 2000,1024));
        window.setViewport(viewport);

        Viewport viewport2 = new Viewport(new FloatRect(400,50, 900,400));


        Time elapsedSinceBeginning = Time.seconds(0);
        while (window.isOpen()) {
            Time elapsed = clk.restart();
            elapsedSinceBeginning.add(elapsed);
            //System.out.println(1.0/elapsed.asSeconds());
            fpsText.setString("fps:"+(int)(1.0/elapsed.asSeconds()));
            //out.println("abs:" + mouse.getAbsolutePosition().x + ":" + mouse.getAbsolutePosition().y);
            //out.println("rel:" + mouse.getRelativePosition().x + ":" + mouse.getRelativePosition().y);

            Event event;
            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                    System.exit(0);
                } else if (event.type == Event.Type.KEYREPEAT) {
                    System.out.println(event.keyRepeated);
                } else if (event.type == Event.Type.MOUSELEAVE) {
                    System.out.print("l");
                } else if (event.type == Event.Type.MOUSESCROLL) {
                    System.out.print("s");
                    shape.move(0,event.scrollY);
                } else if (event.type == Event.Type.MOUSEENTER) {
                    System.out.print("e");
                } else if (event.type == Event.Type.MOUSEDROP) {
                    System.out.print("d");
                } else if (event.type == Event.Type.JOYSTICK_CONNECTION) {
                    System.out.println("Joystick event");
                } else if (event.type == Event.Type.JOYSTICK_DISCONNECTION) {
                    System.out.println("Joystick event");
                } else if (event.type == Event.Type.BUTTONRELEASED) {
                    System.out.print("r");
                } else if (event.type == Event.Type.BUTTONPRESSED) {
                    System.out.print("p");
                } else if (event.type == Event.Type.RESIZE) {
                    System.out.print("R");
                    //camera.setDimension(new Vector2f(window.getDimension()));
                } else if (event.type == Event.Type.MOVE) {
                    System.out.print("m");
                } else if (event.type == Event.Type.FOCUS) {
                    System.out.print("f+");
                } else if (event.type == Event.Type.UNFOCUS) {
                    System.out.print("f-");
                }
            }

            //viewport update
            //viewport.setTopleftCorner(new Vector2f((float)elapsedSinceBeginning.asMilliseconds()/10, (float)elapsedSinceBeginning.asMilliseconds()/10));
            //viewport.setTopleftCorner(new Vector2f(15,15));
            //viewport.setDimension(new Vector2f(window.getDimension()).add(new Vector2f(-30, -30)));
            //viewport2.setDimension(new Vector2f(window.getDimension()).add(new Vector2f(-30, -30)));


            shape.move((float)elapsed.asSeconds()*100, (float)elapsed.asSeconds()*100);
            fullBackground.move(1,1);
            screen.move(0.5f,0);
            screen2.move(0.25f,0);


            camera.setDimension(viewport.getDimension());
            camera.setCenter(shape.getPosition());
            if (keyboard.isKeyPressed(GLFW_KEY_P)) {
                try {
                    renderTexture.capture().saveAs("target.jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (keyboard.isKeyPressed(AZERTYLayout.ESCAPE.getKeyID())) {
                window.close();
                return ;
            }


                //Rendering
            renderTexture.clear(new Color(0,0,1, 0.5f));
            texturedShader.bind();
            renderTexture.getCamera().setUniformMVP(0);
            renderTexture.draw(sprite);
            untexturedShader.bind();
            renderTexture.getCamera().setUniformMVP(0);
            renderTexture.draw(shape);

            screenCamera.move(new Vector2f(0.1f, 0.1f));
            renderTexture2.clear(new Color(1,1,0, 0.5f));
            untexturedShader.bind();
            renderTexture2.getCamera().setUniformMVP(0);
            renderTexture2.draw(background);
            renderTexture2.draw(shape);
            texturedShader.bind();
            renderTexture2.getCamera().setUniformMVP(0);
            renderTexture2.draw(sprite);


            //test viewport 1
            window.setViewport(viewport);
            window.clear(Color.Green);
            window.draw(fullBackground);
            window.draw(shape, texturedShader);

            //texturedShader.bind();
            //window.getCamera().setUniformMVP(0);
            for (int i = 0; i < array.size() ; ++i) {
                window.draw(array.get(i), texturedShader);
            }
            window.draw(screen);
            window.draw(screen2);
            fpsText.setPosition(shape.getPosition().x-200, shape.getPosition().y - 50);
            Shader.unbind();
            fpsText.draw();

            /*screen.draw();
            text.setPosition(shape.getPosition().x, shape.getPosition().y);
            text2.setPosition(shape.getPosition().x, shape.getPosition().y + 100);
            text3.setPosition(shape.getPosition().x, shape.getPosition().y + 200);

            textShape.setPosition(shape.getPosition().x, shape.getPosition().y + 100);
            textShape.draw();
            text.draw();
            text2.draw();
            text3.draw();*/
            //screen2.draw();

            //fpsText.draw();


            //test viewport 2
            /*camera.setCenter(shape.getPosition());
            viewport.setTopleftCorner(new Vector2f(30,30));
            viewport.apply(window);*/
            /*camera.setDimension(viewport2.getDimension());
            window.setViewport(viewport2);
            window.draw(screen);
            window.draw(screen2);
            window.draw(shape);*/

            window.display();
        }

        for (int i = 0; i < array.size() ; ++i) {
            //array.get(i).free();
        }
    }
}

