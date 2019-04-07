 public class Example2D {
   public static void main(String[] args) {
      GLFWWindow window = new RenderWindow(VideoMode.getDesktopMode().width / 10,VideoMode.getDesktopMode().height / 10, "OpenGL", WindowStyle.DEFAULT);
      RectangleShape shape = new RectangleShape(10,10, 10,10);
      shape.setFillColor(Color.Red);

      Mouse mouse = new Mouse(window);

      while (window.isOpen()) {
        //display
        if (mouse.isButtonPressed(Mouse.Button.Left.id)) {
          window.clear(Color.Black);
        }

        shape.draw();

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
            shape.move(0,event.scrollY);
          }
          if (event.type == Event.Type.MOUSEENTER) {
            System.out.println("Mouse enter inside the window");
          }
          if (event.type == Event.Type.MOUSEDROP) {
            System.out.println("Mouse is dropping files");
          }
          if (event.type == Event.Type.JOYSTICK) {
            System.out.println("Joystick event");
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
