package Graphics;


import static org.lwjgl.opengl.GL11.*;

public class Sprite extends Shape {
    private Texture texture = null;
    private float tx=0,ty=0,tw=0,th=0;
    private float width=0,height=0;

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
    }

    public void setTextureRect(float x, float y, float w, float h){
        this.width = w;
        this.height = h;

        tx = x % texture.getWidth();
        ty = y % texture.getHeight();
        tw = w / texture.getWidth();
        th = h / texture.getHeight();
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

            glEnd();

            Texture.unbind();
        }
    }

    public Texture getTexure(){
        return texture;
    }

}
