package Example;

import Graphics.*;

import java.io.IOException;

import System.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;


public class TestGLFWWindow {
    public static void main(String[] args) {

        GLFWWindow window = new GLFWWindow(new VideoMode(500,500), "OpenGL", WindowStyle.DEFAULT/*.remove(WindowStyle.VSYNC)*/, CallbackMode.DEFAULT);
        GLFWWindow window2 = new GLFWWindow(new VideoMode(500,500), "Window 2", WindowStyle.DEFAULT, CallbackMode.DEFAULT, window);
        window2.setPosition(new Vector2i(10,30));


        Shader texturedShader;
        Shader untexturedShader;
        Texture texture;
        try {
            texturedShader = new Shader("shaders/vao/textured/mvp.vert", "shaders/vao/textured/mvp.frag");
            untexturedShader = new Shader("shaders/vao/untextured/mvp.vert", "shaders/vao/untextured/mvp.frag");
            texture = new Texture("Phases.bmp");
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }

        RectangleShape shape = new RectangleShape(10,10, 10,10);
        shape.setFillColor(Color.Red);
        RectangleShape shape2 = new RectangleShape(10,100, 50,50);
        shape2.setFillColor(Color.Yellow);
        Sprite sprite = new Sprite(texture);
        //sprite.setOrigin(sprite.getBounds().w/2.f, sprite.getBounds().h/2.f);
        sprite.setRotation(10.f/180.f*(float)Math.PI);


        window.setActive();

        RectangleShape shape3 = new RectangleShape(10,10, 10,10);
        shape3.setFillColor(Color.Red);
        RectangleShape shape4 = new RectangleShape(10,100, 50,50);
        shape4.setFillColor(Color.Yellow);
        Sprite sprite2 = new Sprite(texture);
        //sprite.setOrigin(sprite.getBounds().w/2.f, sprite.getBounds().h/2.f);
        sprite2.setRotation(90.f/180.f*(float)Math.PI);

        Keyboard keyboard = new Keyboard(window, Keyboard.AZERTY);

        //Camera is not default window camera so it need to be updated when window is resized
        Camera2D tracker = new Camera2D(window);
        window.setCamera(tracker);


        while (window.isOpen()) {
            if (keyboard.isKeyPressed(GLFW_KEY_LEFT)) {
                shape3.move(-1f, 0);
            }
            if (keyboard.isKeyPressed(GLFW_KEY_RIGHT)) {
                shape3.move(1f, 0);
            }
            if (keyboard.isKeyPressed(GLFW_KEY_UP)) {
                shape3.move(0, -1f);
            }
            if (keyboard.isKeyPressed(GLFW_KEY_DOWN)) {
                shape3.move(0, 1f);
            }

            tracker.setCenter(shape3.getPosition());

            Event event;


            window.clear(Color.Blue);
            untexturedShader.bind();
            window.getCamera().setUniformMVP(0);
            window.draw(shape3);
            window.draw(shape4);
            texturedShader.bind();
            window.getCamera().setUniformMVP(0);
            window.draw(sprite2);
            window.display();


            window2.clear();
            untexturedShader.bind();
            window2.getCamera().setUniformMVP(0);
            window2.draw(shape2);
            texturedShader.bind();
            window2.getCamera().setUniformMVP(0);
            window2.draw(sprite);
            window2.display();

            while ((event = window2.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window2.close();
                }
                if (event.type == Event.Type.MOUSEDROP) {
                    System.out.println("Mouse drop! 'window two'");
                }
                if (event.type == Event.Type.KEYPRESSED) {
                    System.out.println("KEYPRESSED! 'window two'");
                }
            }


            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                    window2.close();
                    System.exit(0);
                }
                if (event.type == Event.Type.KEYRELEASED && event.keyReleased == GLFW_KEY_P) {
                    System.out.println("Screenshot! 'capture.png'");
                    try {
                        window.capture().saveAs("capture.png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //Camera is not default window camera so it needs to be updated when window is resized
                if (event.type == Event.Type.RESIZE) {
                    tracker.setDimension(new Vector2f(event.resizeX, event.resizeY));
                }
                if (event.type == Event.Type.MOUSEDROP) {
                    System.out.println("Mouse drop! 'window one'");
                }
                if (event.type == Event.Type.KEYPRESSED) {
                    System.out.println("KEYPRESSED! 'window one'");
                }
            }
        }
    }

}
