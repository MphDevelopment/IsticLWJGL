package Example;

import Graphics.*;

import java.io.IOException;

import System.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;


public class TestGLFWWindow {
    public static void main(String[] args) {
        GLFWWindow window = new GLFWWindow(/*VideoMode.getDesktopMode()*/new VideoMode(500,500), "OpenGL", WindowStyle.DEFAULT/*.remove(WindowStyle.VSYNC)*/, CallbackMode.DEFAULT);
        GLFWWindow window2 = new GLFWWindow(/*VideoMode.getDesktopMode()*/new VideoMode(500,500), "Window 2", WindowStyle.DEFAULT, CallbackMode.DEFAULT, window);
        window2.setPosition(new Vector2i(10,30));

        Texture texture;
        try {
            texture = new Texture("dalle.png");
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }

        RectangleShape shape = new RectangleShape(10,10, 10,10);
        shape.setFillColor(Color.Red);
        RectangleShape shape2 = new RectangleShape(10,100, 50,50);
        shape2.setFillColor(Color.Yellow);
        Sprite sprite = new Sprite(texture);

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

            Event event;


            window2.clear();
            window2.draw(shape2);
            window2.draw(sprite);
            window2.display();

            while ((event = window2.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window2.close();
                }
                if (event.type == Event.Type.MOUSEDROP) {
                    System.out.println("Mouse drop! 'window two'");
                }
            }

            window.clear();
            window.draw(shape2);
            window.draw(shape);
            window.draw(sprite);
            window.display();

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
            }


        }
    }

}
