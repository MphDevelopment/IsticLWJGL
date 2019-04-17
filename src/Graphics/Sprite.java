package Graphics;


import org.lwjgl.util.vector.*;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

/**
 * "Display List" sprite
 */
public class Sprite extends Shape {
    private Texture texture = null;
    private float tx=0,ty=0,tw=0,th=0;

    public Sprite(){}

    public Sprite(Texture texture){
        this.setTexture(texture, true);
    }

    public void setTexture(Texture texture, boolean resize){
        this.texture = texture;
        if (texture != null && resize) {
            width = texture.getWidth();
            height = texture.getHeight();

            tx = ty = 0;
            tw = 1.f;
            th = 1.f;
        }

        update();
    }

    /**
     *
     * @param x texture chosen x-pos [0 to texture.getWidth()-1]
     * @param y texture chosen y-pos [0 to texture.getHeight()-1]
     * @param w texture chosen width
     * @param h texture chosen height
     */
    public void setTextureRect(float x, float y, float w, float h){
        this.width = w;
        this.height = h;

        tx = x % texture.getWidth();
        ty = y % texture.getHeight();
        tw = w / texture.getWidth();
        th = h / texture.getHeight();

        update();
    }

    protected void update() {
        /*float s = Math.sin(radian);
        float c = Math.cos(radian);

        // translate point back to origin:
        p.x -= x + ox;
        p.y -= y + oy;

        new Vector3f().toLwjgl().x+=2;new Matrix4f().translate(new Vector2f(x + ox, y + oy)).rotate(radian, new Vector3f(0,0,1));

        // rotate point
        float xnew = p.x * c - p.y * s;
        float ynew = p.x * s + p.y * c;

        // translate point back:
        p.x = xnew + cx;
        p.y = ynew + cy;
        return p;*/
    }

    public void draw() {
        if (texture != null) {
            texture.bind();

            glEnable(GL_TEXTURE_2D);

            /*glBegin(GL_QUADS);
            glColor3d(color.r,color.g,color.b);
            glTexCoord2d(tx, ty);
            glVertex2d(x - ox, y - oy);
            glTexCoord2d(tx + tw, ty);
            glVertex2d(x + width*sx - ox, y - oy);
            glTexCoord2d(tx + tw, ty + th);
            glVertex2d(x + width*sx - ox, y + height*sy - oy);
            glTexCoord2d(tx, ty + th);
            glVertex2d(x - ox, y + height*sy - oy);*/

            glBegin(GL_QUADS);
            glColor3d(color.r,color.g,color.b);

            glTexCoord2d(tx, ty);
            glVertex2d(x - ox, y + height*sy - oy);
            glTexCoord2d(tx + tw, ty);
            glVertex2d(x + width*sx - ox, y + height*sy - oy);
            glTexCoord2d(tx + tw, ty + th);
            glVertex2d(x + width*sx - ox, y - oy);
            glTexCoord2d(tx, ty + th);
            glVertex2d(x - ox, y - oy);

            glTexCoord2d(tx, ty);
            glVertex2d(x - ox, y + height*sy - oy);
            glTexCoord2d(tx + tw, ty);
            glVertex2d(x + width*sx - ox, y + height*sy - oy);
            glTexCoord2d(tx + tw, ty + th);
            glVertex2d(x + width*sx - ox, y - oy);
            glTexCoord2d(tx, ty + th);
            glVertex2d(x - ox, y - oy);

            glEnd();

            Texture.unbind();
        }
    }

    public Texture getTexure(){
        return texture;
    }

    @Override
    public FloatRect getBounds() {
        return new FloatRect(x - ox, y - oy, width * sx, height * sy);
    }

}
