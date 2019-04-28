package Graphics;


import OpenGL.GLM;

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
        //TODO update each time rotation is made, moving, scaling, ...
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

            //those values must be saved as Sprite parameters (not calculated each time but at each updates)
            double cos = Math.cos(radian);
            double sin = Math.sin(radian);
            Vector2f center = new Vector2f(x, y);
            Vector2f tl = GLM.rotate(center, new Vector2f(x - ox, y - oy), cos, sin);
            Vector2f tr = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy), cos, sin);
            Vector2f bl = GLM.rotate(center, new Vector2f(x - ox, y - oy + sy * height), cos, sin);
            Vector2f br = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy + sy * height), cos, sin);

            glTexCoord2d(tx, ty);
            glVertex2d(bl.x, bl.y);
            glTexCoord2d(tx + tw, ty);
            glVertex2d(br.x, br.y);
            glTexCoord2d(tx + tw, ty + th);
            glVertex2d(tr.x, tr.y);
            glTexCoord2d(tx, ty + th);
            glVertex2d(tl.x, tl.y);

            glEnd();


            /*glBegin(GL_QUADS);
            glColor3d(color.r,color.g,color.b);

            glTexCoord2d(tx, ty);
            glVertex2d(x - ox, y + height*sy - oy);
            glTexCoord2d(tx + tw, ty);
            glVertex2d(x + width*sx - ox, y + height*sy - oy);
            glTexCoord2d(tx + tw, ty + th);
            glVertex2d(x + width*sx - ox, y - oy);
            glTexCoord2d(tx, ty + th);
            glVertex2d(x - ox, y - oy);

            /*glTexCoord2d(tx, ty);
            glVertex2d(x - ox, y + height*sy - oy);
            glTexCoord2d(tx + tw, ty);
            glVertex2d(x + width*sx - ox, y + height*sy - oy);
            glTexCoord2d(tx + tw, ty + th);
            glVertex2d(x + width*sx - ox, y - oy);
            glTexCoord2d(tx, ty + th);
            glVertex2d(x - ox, y - oy);

            glEnd();*/

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
