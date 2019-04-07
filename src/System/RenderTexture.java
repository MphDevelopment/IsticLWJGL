package System;

import Graphics.Color;
import Graphics.Drawable;
import Graphics.Texture;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

@Deprecated
public class RenderTexture extends RenderTarget {
    private int fboId;
    private int depthId;

    private Texture texture = null;

    public RenderTexture(int width, int height) throws IOException {
        /* // The framebuffer, which regroups 0, 1, or more textures, and 0 or 1 depth buffer.
        int FramebufferName = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, FramebufferName);

        // The texture we're going to render to
        int renderedTexture = glGenTextures();

        // "Bind" the newly created texture : all future texture functions will modify this texture
        glBindTexture(GL_TEXTURE_2D, renderedTexture);

        // Give an empty image to OpenGL ( the last "0" )
        glTexImage2D(GL_TEXTURE_2D, 0,GL_RGB, width, height, 0,GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        // Poor filtering. Needed !
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        // The depth buffer
        int depthrenderbuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthrenderbuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthrenderbuffer);

        // Set "renderedTexture" as our colour attachement #0
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, renderedTexture, 0);

        // Set the list of draw buffers.
        ByteBuffer DrawBuffers = ByteBuffer.allocateDirect(1);
        DrawBuffers.put((byte)GL_COLOR_ATTACHMENT0);
        //DrawBuffers.flip();
        glDrawBuffers(DrawBuffers.asIntBuffer()); // "1" is the size of DrawBuffers

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new IOException("");

        this.glId = renderedTexture;
        this.fboId = FramebufferName;
        this.depthId = depthrenderbuffer;

        texture = new Texture(this.glId, width, height);

        //glBindRenderbuffer(GL_RENDERBUFFER, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);*/


        //frame buffer
        this.fboId = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, this.fboId);

        //depth buffer
        this.depthId = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, this.depthId);

        //allocate space for the renderbuffer
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, width, height);

        //attach depth buffer to fbo
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, this.depthId);

        //create texture to render to
        this.glId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, (int)this.glId);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer)null);

        //attach texture to the fbo
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, (int)this.glId, 0);

        //check completeness
        if(glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) != GL_FRAMEBUFFER_COMPLETE_EXT)
            throw new IOException("An error occured creating the frame buffer.");

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

        texture = new Texture((int)this.glId, width, height);
    }

    @Override
    public void clear(Color color) {
       // if (RenderTarget.getCurrentRenderer() != this){
        //    super.setCurrent();
          //  glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);
        //}


        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //glClearColor(color.r, color.g, color.b, color.a);

        //glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void clear() {
        //if (RenderTarget.getCurrentRenderer() != this){
        //    super.setCurrent();
        //    glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);
        //}

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void draw(Drawable d) {
        //if (RenderTarget.getCurrentRenderer() != this){
        //    super.setCurrent();
            //glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);
            //glBindFramebuffer(GL_FRAMEBUFFER, 0);
        //}

        d.draw();

        //glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void display(){
        glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);

        glFlush();

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }


    public void bind(){
        //render to fbo
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, this.fboId);
        glPushAttrib(GL_VIEWPORT_BIT);
        glViewport(0, 0, texture.getWidth(), texture.getHeight());

        //glBindTexture(GL_TEXTURE_2D, texture.getGlId());
    }

    public void unbind(){
        //glBindTexture(GL_TEXTURE_2D, 0);

        glPopAttrib();

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
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
}
