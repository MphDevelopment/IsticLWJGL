package Graphics;


import OpenGL.GLM;

import static org.lwjgl.opengl.GL11.*;


public class Sprite extends Shape {
    private ConstTexture texture = null;
    private float tx=0,ty=0,tw=0,th=0;

    public Sprite(){
        //buffer = new VertexBuffer(4, 3, new int[]{3,4,2}, VertexBuffer.Mode.QUADS, VertexBuffer.Usage.STREAM);
        buffer = new VertexBuffer(6, 3, new int[]{3,4,2}, VertexBuffer.Mode.TRIANGLES, VertexBuffer.Usage.STREAM);
        update();
        updateColor();
        updateCoords();
    }

    public Sprite(ConstTexture texture){
        this();
        this.setTexture(texture, true);
        updateColor();
    }

    public void setTexture(ConstTexture texture, boolean resize){
        this.texture = texture;
        if (texture != null && resize) {
            width = texture.getWidth();
            height = texture.getHeight();

            tx = ty = 0;
            tw = 1.f;
            th = 1.f;

            updateCoords();
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

        glTexCoord2d(tx, ty + th);
        glTexCoord2d(tx + tw, ty + th);
        glTexCoord2d(tx + tw, ty);
        glTexCoord2d(tx, ty);

        update();
        updateCoords();
    }

    protected void update() {
        if (buffer == null) return ;
        //TODO update each time rotation is made, moving, scaling, ...
        //those values must be saved as Sprite parameters (not calculated each time but at each updates)
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
    protected void updateColor(){
        if (buffer != null) buffer.update(1, new float[]{
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a});
    }


    /*@Override
    protected void updateColor(){
        if (buffer != null) buffer.update(1, new float[]{
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a,
                color.r,color.g,color.b,color.a
        });
    }*/

    private void updateCoords(){
        if (buffer == null) return ;

        /*buffer.update(2, new float[]{
                tx,ty+th,
                tx+tw,ty+th,
                tx+tw,ty,
                tx,ty});*/

        buffer.update(2, new float[]{
                tx,ty+th,
                tx+tw,ty+th,
                tx+tw,ty,

                tx,ty,
                tx+tw,ty,
                tx,ty+th,

        });
    }

    public void draw() {
        if (buffer == null) return ;

        if (texture != null) {
            texture.bind();

            //glEnable(GL_TEXTURE_2D);

            buffer.draw();

            /*glBegin(GL_QUADS);
            glColor4f(color.r,color.g,color.b,color.a);

            //those values must be saved as Sprite parameters (not calculated each time but at each updates)
            double cos = Math.cos(radian);
            double sin = Math.sin(radian);
            Vector2f center = new Vector2f(x, y);
            Vector2f tl = GLM.rotate(center, new Vector2f(x - ox, y - oy), cos, sin);
            Vector2f tr = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy), cos, sin);
            Vector2f bl = GLM.rotate(center, new Vector2f(x - ox, y - oy + sy * height), cos, sin);
            Vector2f br = GLM.rotate(center, new Vector2f(x - ox + sx * width, y - oy + sy * height), cos, sin);

            glTexCoord2d(tx, ty + th);
            glVertex2d(bl.x, bl.y);
            glTexCoord2d(tx + tw, ty + th);
            glVertex2d(br.x, br.y);
            glTexCoord2d(tx + tw, ty);
            glVertex2d(tr.x, tr.y);
            glTexCoord2d(tx, ty);
            glVertex2d(tl.x, tl.y);

            glEnd();*/

            //too greedy
            //Texture.unbind();
        }
    }

    public ConstTexture getTexture(){
        return texture;
    }

    @Override
    public FloatRect getBounds() {
        return new FloatRect(x - ox, y - oy, width * sx, height * sy);
    }

}
