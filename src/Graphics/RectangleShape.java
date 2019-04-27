package Graphics;

import OpenGL.GLM;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

/**
 * "Display list" rectangle shape
 */
public class RectangleShape extends Shape {
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
    protected void update(){

    }

    @Override
    public void draw() {
        /*glBegin(GL_QUADS);
        glColor3d(color.r,color.g,color.b);
        glVertex2d(x - ox, y - oy);
        glVertex2d(x - ox, y + height - oy);
        glVertex2d(x + width - ox, y + height - oy);
        glVertex2d(x + width - ox, y - oy);
        glEnd();*/

        glBegin(GL_QUADS);
        glColor3d(color.r,color.g,color.b);

        //those values must be saved as Sprite parameters (not calculated each time but at each updates)
        double cos = Math.cos(radian);
        double sin = Math.sin(radian);
        Vector2f center = new Vector2f(x + ox, y + oy);
        Vector2f tl = GLM.rotate(center, new Vector2f(x - ox, y - oy), cos, sin);
        Vector2f tr = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy), cos, sin);
        Vector2f bl = GLM.rotate(center, new Vector2f(x - ox, y - oy + sy * height), cos, sin);
        Vector2f br = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy + sy * height), cos, sin);

        glVertex2d(bl.x, bl.y);
        glVertex2d(br.x, br.y);
        glVertex2d(tr.x, tr.y);
        glVertex2d(tl.x, tl.y);

        glEnd();
    }

    @Override
    public FloatRect getBounds() {
        return new FloatRect(x - ox, y - oy, width * sx, height * sy);
    }
}
