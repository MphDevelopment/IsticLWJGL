package Graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;


import System.GlObject;

import javax.imageio.ImageIO;

/**
 * Texture represents a 2D graphic RGBA image that can be displayed by GPU.
 * Contrary to Image, data are saved into the DRAM.
 * @see Graphics.Image Image (RAM only)
 */
public class Texture extends GlObject {
    // Image dimension
    public final int width;
    public final int height;

    // Native OpenGL Filter Modes
    public static final int LINEAR = GL_LINEAR;
    public static final int NEAREST = GL_NEAREST;

    // Native OpenGL Wrap Modes
    public static final int CLAMP = GL_CLAMP;
    public static final int CLAMP_TO_EDGE = GL_CLAMP_TO_EDGE;
    public static final int REPEAT = GL_REPEAT;
    public static final int MIRROR = GL_MIRRORED_REPEAT;

    /**
     * Constructor called when the texture is added to the DRAM by another means
     * @param glId OpenGl id
     * @param width pixel width
     * @param height pixel height
     */
    //TODO must be protected and then used by RenderTexture (that will inherit from Texture) [can't inherit from 2 classes...]
    public Texture(int glId, int width, int height){
        this.glId = glId;
        this.width = width;
        this.height = height;
    }

    /**
     * Loads a texture using a PNG file path
     * @param file PNG file path
     *             filter mode is by default GL_NEAREST
     *             wrap mode is by default GL_CLAMP_TO_EDGE
     * @throws IOException throws when texture is not correctly loaded because the file does not exist or because of internal OpenGL issues
     */
    public Texture(String file) throws IOException {
        this(file, GL_NEAREST);
    }

    /**
     * Loads a texture using a PNG file path with a specific Filter
     * @param file PNG file path
     * @param filter OpenGL native filter mode
     *               wrap mode is by default GL_CLAMP_TO_EDGE.
     * @throws IOException throws when texture is not correctly loaded because the file does not exist or because of internal OpenGL issues
     */
    public Texture(String file, int filter) throws IOException {
        this(file, filter, GL_CLAMP);
    }

    /**
     * Loads a texture using a PNG file path with a specific Filter and a specific Wrap Mode
     * @param file PNG file path
     * @param filter OpenGL native filter mode
     * @param wrap OpenGL native wrap mode
     * @throws IOException throws when texture is not correctly loaded because the file does not exist or because of internal OpenGL issues
     */
    public Texture(String file, int filter, int wrap) throws IOException {
        InputStream input = null;
        try {
            /*//get an InputStream from our URL
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
            buf.flip();*/

            input = new FileInputStream(file);

            BufferedImage i = ImageIO.read(input);

            width = i.getWidth();
            height = i.getHeight();

            ByteBuffer buf = Image.convertImage(i);

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
        } finally {
            if (input != null) {
                try { input.close(); } catch (IOException e) { }
            }
        }
    }

    private static ByteBuffer convertImage(BufferedImage image)
    {
        final int BYTES_PER_PIXEL = 4;
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);

        for(int y = 0; y < image.getHeight(); y++)
        {
            for(int x = 0; x < image.getWidth(); x++)
            {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip();

        return buffer;
    }

    /**
     * Sets this (glId) as the current Texture
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, (int)glId);
    }

    /**
     * Removes last bound texture as current texture
     */
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Current texture's width in pixels
     * @return image pixel width
     */
    public int getWidth(){
        return width;
    }

    /**
     * Current texture's height in pixels
     * @return image pixel height
     */
    public int getHeight(){
        return height;
    }

    /**
     * If enabled GPU will repeat texture if the texture coordinates exceeds the texture dimension
     * @param repeated enable/disable repetition
     */
    public void setRepeated(boolean repeated) {
        //bind texture
        this.bind();

        //setup parameters
        int wrap = (repeated) ? GL_REPEAT : GL_CLAMP_TO_EDGE;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);

        Texture.unbind();
    }

    /**
     * If enabled GPU will smooth the texture aspect
     * @param smooth enable/disable repetition
     */
    public void setSmooth(boolean smooth) {
        //bind texture
        this.bind();

        //setup parameters
        int filter = (smooth) ? GL_LINEAR : GL_NEAREST;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);

        Texture.unbind();
    }

    /**
     * Defines a specific OpenGL filter mode for the texture
     * @param filter the specified filter
     */
    public void setFilterMode(int filter){
        //bind texture
        this.bind();

        //setup parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);

        Texture.unbind();
    }

    /**
     * Defines a specific OpenGL wrap mode for the texture
     * @param wrap the specified wrap mode
     */
    public void setWrapMode(int wrap){
        //bind texture
        this.bind();

        //setup parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);

        Texture.unbind();
    }

    /**
     * Frees the texture from GPU memory.
     */
    @Override
    public void free(){
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures((int)glId);
        glId = 0;
    }

    /**
     * Tests if two textures are same by comparing their 'glId'. Both texture must be into DRAM (it means that their 'glId' must be different to 0).
     * @param o comparison object
     * @return true if both texture are into DRAM and have same 'glId'
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Texture) {
            Texture t = (Texture)o;
            return (t.getGlId() == this.getGlId()) && this.getGlId() != 0;
        }

        return false;
    }

    public Image toImage() {
        final int bpp = 4;

        glBindTexture(GL_TEXTURE_2D, (int)glId);

        byte[] array = new byte[width * height * 4];
        ByteBuffer buffer = ByteBuffer.allocateDirect(array.length);
        glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        buffer.get(array);

        //invert y
        /*for (int i=0;i < width ; ++i) {
            for (int j=0; j < height/2 ; ++j) {
                byte[] rgba = new byte[]{
                        array[i * bpp + j * width * bpp    ],
                        array[i * bpp + j * width * bpp + 1],
                        array[i * bpp + j * width * bpp + 2],
                        array[i * bpp + j * width * bpp + 3]
                };

                array[i * bpp + j * width * bpp    ] = array[i * bpp + (height - j - 1) * width * bpp    ];
                array[i * bpp + j * width * bpp + 1] = array[i * bpp + (height - j - 1) * width * bpp + 1];
                array[i * bpp + j * width * bpp + 2] = array[i * bpp + (height - j - 1) * width * bpp + 2];
                array[i * bpp + j * width * bpp + 3] = array[i * bpp + (height - j - 1) * width * bpp + 3];

                array[i * bpp + (height - j - 1) * width * bpp    ] = rgba[0];
                array[i * bpp + (height - j - 1) * width * bpp + 1] = rgba[1];
                array[i * bpp + (height - j - 1) * width * bpp + 2] = rgba[2];
                array[i * bpp + (height - j - 1) * width * bpp + 3] = rgba[3];
            }
        }*/

        return new Image(array, width, height);
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