package System;

import Graphics.*;
import OpenGL.GLM;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderTexture extends RenderTarget {
    private int fboId;
    private int depthId;

    private Texture texture = null;

    public RenderTexture(int width, int height) throws IOException {
        super();

        //frame buffer
        this.fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);

        //depth buffer
        this.depthId = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, this.depthId);

        //allocate space for the renderbuffer
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);

        //attach depth buffer to fbo
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, this.depthId);

        //create texture to render to
        this.glId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, (int)this.glId);
        //glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        // Poor filtering. Needed !
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer)null);

        //attach texture to the fbo
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, (int)this.glId, 0);

        //check completeness
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new IOException("An error occured creating the frame buffer.");

        texture = new Texture((int)this.glId, width, height);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    @Override
    public void clear(Color color) {
        //this.bind();

        glClearColor(color.r, color.g, color.b, color.a);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void clear() {
        //this.bind();

        glClearColor(0, 0,0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    }

    @Override
    public void draw(Drawable d) {
        //this.bind();

        d.draw();
    }

    public void display(){
        //this.bind();

        glFlush();

        //this.unbind();
    }

    //TODO on doit faire en sorte que les matrices se mettent a jour entre chaque different bind de RenderTarget
    public void bind(){
        //render to fbo
        //if (RenderTarget.getCurrentRenderer() != this) {
            //super.setCurrent();
        glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);
        glViewport(0, 0, texture.getWidth(), texture.getHeight());
        //}

        glMatrixMode(GL_PROJECTION);
        Matrix4f ortho = GLM.ortho(0.f, texture.getWidth(), texture.getHeight(), 0.f, -1f, 1.f);
        glLoadMatrixf(GLM.toFloatArray(ortho));

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void unbind(){
        //if (RenderTarget.getCurrentRenderer() == this) {
            //glPopAttrib();
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
       // }
    }

    public Vector2i getDimension() {
        return new Vector2i(texture.getWidth(), texture.getHeight());
    }

    public Texture getTexture(){
        return texture;
    }

    @Override
    public void free() {
        if (texture != null)
            texture.free();
        texture = null;
    }

    public Image capture() {
        final int bpp = 4;

        this.bind();

        glReadBuffer(GL_FRONT);
        ByteBuffer buffer = BufferUtils.createByteBuffer(texture.getWidth() * texture.getHeight() * bpp);

        glReadPixels(0, 0, texture.getWidth(), texture.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        Image i = new Image(buffer, texture.getWidth(), texture.getHeight());

        this.unbind();

        return i;
    }
}
