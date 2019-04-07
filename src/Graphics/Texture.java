package Graphics;
/**
 * Copyright (c) 2012, Matt DesLauriers All rights reserved.
 *
 *	Redistribution and use in source and binary forms, with or without
 *	modification, are permitted provided that the following conditions are met:
 *
 *	* Redistributions of source code must retain the above copyright notice, this
 *	  list of conditions and the following disclaimer.
 *
 *	* Redistributions in binary
 *	  form must reproduce the above copyright notice, this list of conditions and
 *	  the following disclaimer in the documentation and/or other materials provided
 *	  with the distribution.
 *
 *	* Neither the name of the Matt DesLauriers nor the names
 *	  of his contributors may be used to endorse or promote products derived from
 *	  this software without specific prior written permission.
 *
 *	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *	AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *	ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *	LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *	CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *	SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *	INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *	CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *	ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *	POSSIBILITY OF SUCH DAMAGE.
 */

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;

import System.GlObject;

public class Texture extends GlObject {
    private static Texture currentTexture;

    public final int width;
    public final int height;

    public static final int LINEAR = GL_LINEAR;
    public static final int NEAREST = GL_NEAREST;

    public static final int CLAMP = GL_CLAMP;
    public static final int CLAMP_TO_EDGE = GL_CLAMP_TO_EDGE;
    public static final int REPEAT = GL_REPEAT;
    public static final int MIRROR = GL_MIRRORED_REPEAT;

    public Texture(int glId, int width, int height){
        this.glId = glId;
        this.width = width;
        this.height = height;
    }

    public Texture(int width, int height){
        this.width = width;
        this.height = height;
    }

    public Texture(String file) throws IOException {
        this(file, GL_NEAREST);
    }

    public Texture(String file, int filter) throws IOException {
        this(file, filter, GL_CLAMP_TO_EDGE);
    }

    public Texture(String file, int filter, int wrap) throws IOException {
        InputStream input = null;
        try {
            //get an InputStream from our URL
            input = new FileInputStream(file);

            //initialize the decoder
            PNGDecoder dec = new PNGDecoder(input);

            //set up image dimensions
            width = dec.getWidth();
            height = dec.getHeight();

            //we are using RGBA, i.e. 4 components or "bytes per pixel"
            final int bpp = 4;

            //create a new byte buffer which will hold our pixel data
            ByteBuffer buf = BufferUtils.createByteBuffer(bpp * width * height);

            //decode the image into the byte buffer, in RGBA format
            dec.decode(buf, width * bpp, PNGDecoder.Format.RGBA);

            //flip the buffer into "read mode" for OpenGL
            buf.flip();

            //enable textures and generate an ID
            //glEnable(GL_TEXTURE_2D);
            glId = glGenTextures();

            //bind texture
            this.bind();

            //setup unpack mode
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            //setup parameters
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);

            //pass RGBA data to OpenGL
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

            Texture.unbind();

            this.setWrapMode(CLAMP);
        } finally {
            if (input != null) {
                try { input.close(); } catch (IOException e) { }
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, (int)glId);
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void setRepeated(boolean repeated) {
        //bind texture
        this.bind();

        //setup parameters
        int wrap = (repeated) ? GL_REPEAT : GL_CLAMP_TO_EDGE;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);

        Texture.unbind();
    }

    public void setSmooth(boolean smooth) {
        //bind texture
        this.bind();

        //setup parameters
        int filter = (smooth) ? GL_LINEAR : GL_NEAREST;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);

        Texture.unbind();
    }

    public void setFilterMode(int filter){
        //bind texture
        this.bind();

        //setup parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);

        Texture.unbind();
    }

    public void setWrapMode(int wrap){
        //bind texture
        this.bind();

        //setup parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);

        Texture.unbind();
    }

    public static void setCurrentTexture(Texture texture){
        currentTexture = texture;
    }

    public static final Texture CurrentTexture(){
        return currentTexture;
    }

    @Override
    public void free(){
        glBindTexture((int)glId, 0);
        glDeleteTextures((int)glId);
        glId = 0;
    }

}

/*

import com.sun.javafx.iio.png.PNGImageLoader2;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import sun.awt.image.PNGImageDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Texture {
    private int glId;
    private int width, height;
    private boolean loaded = false;

    public Texture(){}

    public boolean loadFromFile(String file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            loaded = this.loadFromStream(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            loaded = false;
        }

        return loaded;
    }

    public boolean loadFromStream(InputStream stream) {
        //load png file
        try {
            PNGDecoder decoder = new PNGDecoder(stream);

            //create a byte buffer big enough to store RGBA values
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

            width = decoder.getWidth();
            height = decoder.getHeight();

            //decode
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

            buffer.flip();

            this.bind(buffer);

            //Generally a good idea to enable texturing first
            glEnable(GL_TEXTURE_2D);

            //generate a texture handle or unique ID for this texture
            glId = glGenTextures();

            //bind the texture
            glBindTexture(GL_TEXTURE_2D, glId);

            //use an alignment of 1 to be safe
            //this tells OpenGL how to unpack the RGBA bytes we will specify
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            //set up our texture parameters
                //Setup filtering, i.e. how OpenGL will interpolate the pixels when scaling up or down
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

                //Setup wrap mode, i.e. how OpenGL will handle pixels outside of the expected range
                //Note: GL_CLAMP_TO_EDGE is part of GL12
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            //upload our ByteBuffer to GL
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buf);
        } catch (IOException e) {
            e.printStackTrace();
            return loaded = false;
        }
        //flip the buffer so its ready to read
        return loaded = true;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, glId);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

}
*/