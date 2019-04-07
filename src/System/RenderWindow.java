package System;


import Graphics.*;
import Graphics.Color;

import java.io.IOException;
import java.nio.ByteBuffer;

import static java.lang.System.out;
import static org.lwjgl.opengl.GL11.*;

public class RenderWindow extends GLFWWindow {

    public RenderWindow(int width, int height, String title) {
        super(width, height, title);
    }

    public RenderWindow(int width, int height, String title, WindowStyle style) {
        super(width, height, title, style);
    }

    @Override
    protected void initGl() {
        super.initGl();
    }

    @Override
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

    @Override
    public void draw(Drawable d) {
        if (!this.isOpen()) return ;

        d.draw();
    }

    public static void main(String[] args) {
        GLFWWindow window = new RenderWindow(VideoMode.getDesktopMode().width / 10,VideoMode.getDesktopMode().height / 10, "OpenGL", WindowStyle.DEFAULT);

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

        out.println(texture.getGlId());

        RectangleShape shape = new RectangleShape(10,10, 10,10);
        shape.setFillColor(Color.Red);

        Mouse mouse = new Mouse(window);

        while (window.isOpen()) {
            //texture.bind();
            shape.move(+0.1f,+0.1f);


            if (mouse.isButtonPressed(Mouse.Button.X1.id)) {
                window.hide();
            }
            if (mouse.isButtonPressed(Mouse.Button.X2.id)) {
                window.show();
            }

            if (mouse.isButtonPressed(Mouse.Button.Left.id)) {
                window.clear(Color.Yellow);
            }
            if (mouse.isButtonPressed(Mouse.Button.Right.id)) {
                window.clear(Color.Green);
            }

            shape.draw();

            window.display();

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
                    //
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
                if (event.type == Event.Type.MOUSEDROP) {
                    System.out.print("D");
                }
            }
        }

    }
}
