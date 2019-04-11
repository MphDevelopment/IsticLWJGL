package System;


import Graphics.*;
import Graphics.Color;

import java.io.IOException;

import static java.lang.Math.PI;
import static org.lwjgl.opengl.GL11.*;

public class RenderWindow extends GLFWWindow {

    public RenderWindow(VideoMode videoMode, String title) {
        super(videoMode, title);
    }

    public RenderWindow(VideoMode videoMode, String title, WindowStyle style) {
        super(videoMode, title, style);
    }

    public RenderWindow(VideoMode videoMode, String title, WindowStyle style, CallbackMode modes) {
        super(videoMode, title, style, modes);
    }

    @Override
    public void clear(){
        if (!this.isOpen()) return ;
        glClearColor(0,0,0,1);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void clear(Color color){
        if (!this.isOpen()) return ;

        glClearColor(color.r,color.g,color.b,color.a);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void draw(Drawable d) {
        if (!this.isOpen()) return ;

        d.draw();
    }



    public static void main(String[] args) {
        GLFWWindow window = new RenderWindow(
                new VideoMode(500, 600), "OpenGL",
                WindowStyle.DEFAULT.add(WindowStyle.TOPMOST),
                CallbackMode.DEFAULT
        );

        RenderTexture renderTexture;
        RenderTexture renderTexture2;

        Texture texture;
        try {
            renderTexture = new RenderTexture(200,200);
            renderTexture2 = new RenderTexture(200,200);

            texture = new Texture("dalle.png");
            texture.setWrapMode(Texture.REPEAT);
        } catch (IOException e) {
            window.close();
            return ;
        }


        RectangleShape shape = new RectangleShape(10,10, 10,10);
        shape.setFillColor(Color.Red);

        Sprite sprite = new Sprite(texture);
        sprite.move(50, 50);

        Sprite screen = new Sprite(renderTexture.getTexture());
        screen.setTextureRect(0,0,400,400);
        screen.move(50,50);
        screen.setScale(0.5f,0.5f);

        Sprite screen2 = new Sprite(renderTexture2.getTexture());
        screen2.setTextureRect(0,0,200,200);
        screen.move(30,300);

        Mouse mouse = new Mouse(window);

        Camera2D camera = new Camera2D(window);
        //camera.setZoom(0.25f);
        //camera.setRotation(2*(float)PI);
        camera.setRotation(0.5f);
        Camera2D screenCamera = new Camera2D(renderTexture);
        Camera2D screenCamera2 = new Camera2D(renderTexture2);

        RectangleShape background = new RectangleShape(200,200);
        background.setFillColor(Color.Blue);

        Clock clk = new Clock();

        while (window.isOpen()) {
            Time elapsed = clk.restart();
            //System.out.println(1.0/elapsed.asSeconds());

            Event event;
            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                    System.exit(0);
                }
                if (event.type == Event.Type.MOUSELEAVE) {
                    System.out.print("l");
                }
                if (event.type == Event.Type.MOUSESCROLL) {
                    System.out.print("s");
                    shape.move(0,event.scrollY);
                }
                if (event.type == Event.Type.MOUSEENTER) {
                    System.out.print("e");
                }
                if (event.type == Event.Type.MOUSEDROP) {
                    System.out.print("d");
                }
                if (event.type == Event.Type.JOYSTICK) {
                    System.out.print("j");
                }
                if (event.type == Event.Type.BUTTONRELEASED) {
                    System.out.print("r");
                }
                if (event.type == Event.Type.BUTTONPRESSED) {
                    System.out.print("p");
                }
                if (event.type == Event.Type.RESIZE) {
                    System.out.print("R");
                    camera.setDimension(new Vector2f(window.getDimension()));
                }
                if (event.type == Event.Type.MOVE) {
                    System.out.print("m");
                }
                if (event.type == Event.Type.FOCUS) {
                    System.out.print("f+");
                }
                if (event.type == Event.Type.UNFOCUS) {
                    System.out.print("f-");
                }
            }


            shape.move(+1f,+1f);
            screen.move(0.5f,0);
            screen2.move(0.25f,0);

            renderTexture.bind();
            screenCamera.apply(renderTexture);
            renderTexture.clear(Color.Blue);
            sprite.draw();
            shape.draw();
            renderTexture.unbind();

            renderTexture2.bind();
            screenCamera2.apply(renderTexture2);
            renderTexture2.clear(Color.Yellow);
            background.draw();
            sprite.draw();
            shape.draw();
            renderTexture2.unbind();

            //camera.moveView(new Vector2f(0.5f,0f));
            camera.setCenter(shape.getPosition());
            //camera.setZoom(2);
            //camera.setRotation(1f);
            camera.apply(window);
            window.clear(Color.Transparent);
            screen.draw();
            screen2.draw();
            shape.draw();
            window.display();



        }

    }
}

