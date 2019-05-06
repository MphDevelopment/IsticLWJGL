package Example;

import Graphics.*;
import System.*;

import java.io.IOException;

public final class Example2D {
    public static void main(String[] args) throws IOException {
        GLFWWindow window = new GLFWWindow(new VideoMode(500, 500), "Use mouse left button to clear the screen!", WindowStyle.DEFAULT, CallbackMode.DEFAULT);
        RectangleShape shape = new RectangleShape(10, 10, 10, 10);
        shape.setFillColor(Color.Red);

        Mouse mouse = new Mouse(window);


        Joystick joystick = (Joystick.isPlugged(0) ? new Joystick(0) : null);


        while (window.isOpen()) {
            //display
            if (mouse.isButtonPressed(Mouse.Button.Left)) {
                window.clear(Color.Black);
            }

            if (joystick != null) {
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.A)) {
                    window.clear(Color.Red);
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.B)) {
                    window.clear(Color.Yellow);
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.Y)) {
                    window.clear(Color.White);
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.X)) {
                    window.clear(Color.Black);
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.DOWN)) {
                    window.clear(Color.Blue);
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.RIGHT)) {
                    window.clear(Color.Magenta);
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.LEFT)) {
                    window.clear(Color.Green);
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.UP)) {
                    window.clear(Color.Cyan);
                }

                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.START)) {
                    window.clear(new Color(0.5f,1.f,0.5f));
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.BACK)) {
                    window.clear(new Color(1.0f,0.5f,0.5f));
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.LEFT_THUMB)) {
                    window.clear(new Color(0.5f,0.5f,1.f));
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.RIGHT_THUMB)) {
                    window.clear(new Color(1.f,0.5f,1.f));
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.LEFT_BUMPER)) {
                    window.clear(new Color(0.5f,0.75f,0.25f));
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.RIGHT_BUMPER)) {
                    window.clear(new Color(0.7f,0.10f,0.50f));
                }
                if (joystick.isGamePadButtonPressed(Joystick.GamePadButton.GUIDE)) {
                    window.clear(/*new Color(0.5f,0.5f,0.5f, 0.5f)*/Color.Red);
                }

                System.out.println(joystick.getJoystickName()+":"+joystick.getGamePadAxisValue(Joystick.GamePadAxis.LEFT_AXIS_X, 0.4f)+
                        ";"+joystick.getGamePadAxisValue(Joystick.GamePadAxis.LEFT_AXIS_Y, 0.4f));
                /*System.out.println("right:"+joystick.getGamePadAxisValue(Joystick.GamePadAxis.RIGHT_AXIS_X, 0.4f)+
                        ";"+joystick.getGamePadAxisValue(Joystick.GamePadAxis.RIGHT_AXIS_Y, 0.4f));
                System.out.println("R2:"+joystick.getGamePadAxisValue(Joystick.GamePadAxis.R2, 0.f));
                System.out.println("L2:"+joystick.getGamePadAxisValue(Joystick.GamePadAxis.L2, 0.f));*/
            }
            shape.move(1.f, 1.f);

            window.draw(shape);
            window.display();

            //poll events
            Event event;
            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                    System.exit(0);
                }
                if (event.type == Event.Type.MOUSELEAVE) {
                    System.out.println("Mouse leave the window");
                }
                if (event.type == Event.Type.MOUSESCROLL) {
                    System.out.println("Mouse is scrolling");
                    shape.move(0, event.scrollY);
                }
                if (event.type == Event.Type.MOUSEENTER) {
                    System.out.println("Mouse enter inside the window");
                }
                if (event.type == Event.Type.MOUSEDROP) {
                    System.out.println("Mouse is dropping files");
                }
                if (event.type == Event.Type.JOYSTICK_CONNECTION) {
                    joystick = new Joystick(event.joystick);
                    System.out.println("Joystick "+event.joystick+" connection");
                }
                if (event.type == Event.Type.JOYSTICK_DISCONNECTION) {
                    joystick = null;
                    System.out.println("Joystick "+event.joystick+" disconnection");
                }
                if (event.type == Event.Type.BUTTONRELEASED) {
                    System.out.println("Button released detected");
                }
                if (event.type == Event.Type.BUTTONPRESSED) {
                    System.out.println("Button pressed detected");
                }
                if (event.type == Event.Type.RESIZE) {
                    System.out.println("Window resized");
                    //
                }
                if (event.type == Event.Type.MOVE) {
                    System.out.println("Window moved");
                }
                if (event.type == Event.Type.FOCUS) {
                    System.out.println("Window gained focus");
                }
                if (event.type == Event.Type.UNFOCUS) {
                    System.out.println("Window lost focus");
                }
            }
        }
    }
}
