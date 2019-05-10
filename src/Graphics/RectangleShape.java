package Graphics;

import OpenGL.GLM;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;


public class RectangleShape extends Shape {
    public RectangleShape(){
        this(0,0);
    }

    public RectangleShape(float w, float h){
        this(0,0, w, h);
    }

    public RectangleShape(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        /*buffer = new VertexBuffer(4, 3, new int[]{3,4,2}, VertexBuffer.Mode.QUADS, VertexBuffer.Usage.STREAM);
        buffer.update(2, new float[]{
                0,0,
                1,0,
                1,1,
                0,1
        });*/

        buffer = new VertexBuffer(6, 3, new int[]{3,4,2}, VertexBuffer.Mode.TRIANGLES, VertexBuffer.Usage.STREAM);
        buffer.update(2, new float[]{
                0,0,
                0,1,
                1,1,

                1,1,
                0,0,
                1,0
        });

        /*buffer = new VertexBuffer(4, 2, new int[]{3,4}, VertexBuffer.Mode.QUADS, VertexBuffer.Usage.STREAM);
        buffer.update(2, new float[]{
                0,0,
                1,0,
                1,1,
                0,1
        });*/
        update();
    }

    public void setSize(float x, float y){
        this.width = x;
        this.height = y;
        update();
    }

    @Override
    protected void updateColor(){
        if (buffer != null) buffer.update(1, new float[]{
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a});
    }

    @Override
    protected void update(){
        if (buffer == null) return ;
        //TODO update each time rotation is made, moving, scaling, ...
        //those values must be saved as Sprite parameters (not calculated each time but at each updates)
        //double cos = Math.cos(radian);
        //double sin = Math.sin(radian);
        Vector2f center = new Vector2f(x, y);
        Vector2f tl = GLM.rotate(center, new Vector2f(x - ox, y - oy), cos, sin);
        Vector2f tr = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy), cos, sin);
        Vector2f bl = GLM.rotate(center, new Vector2f(x - ox, y - oy + sy * height), cos, sin);
        Vector2f br = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy + sy * height), cos, sin);

        //buffer.update(0, new float[]{ bl.x, bl.y, 0, br.x, br.y, 0, tr.x, tr.y, 0, tl.x, tl.y, 0});
        buffer.update(0, new float[]{
                bl.x, bl.y, 0,
                br.x, br.y, 0,
                tr.x, tr.y, 0,

                tl.x, tl.y, 0,
                tr.x, tr.y, 0,
                bl.x, bl.y, 0
        });
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
        //glDisable(GL_TEXTURE_2D);

        //Texture.unbind();
        Texture.DefaultTexture().bind();

        //glEnable(GL_TEXTURE_2D);

        buffer.draw();

        /*glBegin(GL_QUADS);
        glColor3d(color.r,color.g,color.b);

        //those values must be saved as Sprite parameters (not calculated each time but at each updates)
        double cos = Math.cos(radian);
        double sin = Math.sin(radian);
        Vector2f center = new Vector2f(x, y);
        Vector2f tl = GLM.rotate(center, new Vector2f(x - ox, y - oy), cos, sin);
        Vector2f tr = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy), cos, sin);
        Vector2f bl = GLM.rotate(center, new Vector2f(x - ox, y - oy + sy * height), cos, sin);
        Vector2f br = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy + sy * height), cos, sin);

        glVertex2d(bl.x, bl.y);
        glVertex2d(br.x, br.y);
        glVertex2d(tr.x, tr.y);
        glVertex2d(tl.x, tl.y);

        glEnd();*/
    }

    @Override
    public FloatRect getBounds() {
        return new FloatRect(x - ox, y - oy, width * sx, height * sy);
    }
}
