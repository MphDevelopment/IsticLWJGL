package Graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class RectangleShape extends Shape {
    private float width=0,height=0;

    public RectangleShape(){}

    public RectangleShape(float w, float h){
        this.width = w;
        this.height = h;
    }

    public RectangleShape(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public void setSize(float x, float y){
        this.width = x;
        this.height = y;
    }

    @Override
    public void draw() {
        glBegin(GL_QUADS);
        glColor3d(color.r,color.g,color.b);
        glVertex2d(x - ox, y - oy);
        glVertex2d(x - ox, y + height - oy);
        glVertex2d(x + width - ox, y + height - oy);
        glVertex2d(x + width - ox, y - oy);
        glEnd();
    }
}
