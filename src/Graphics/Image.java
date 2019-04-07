package Graphics;


import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Image represents a 2D graphic RGBA8 image that can be edit by the CPU.
 * Contrary to Texture it can not be sent to the GPU to be drawn.
 * Contrary to Texture data are saved into the RAM.
 * @apiNote RAM only
 * @see Graphics.Texture DRAM only
 */
public class Image {
    private static final int bpp = 4;

    private int width;
    private int height;
    private float[] buffer;

    public Image(){
        width = 0;
        height = 0;
        buffer = null;
    }

    public Image(int w, int h) {
        this.create(w,h);
    }

    public void create(int w, int h) {
        width = w;
        height = h;

        buffer = new float[w*h*bpp];
        for (int i=0 ; i < buffer.length ; ++i) {
            buffer[0] = 0;
        }
    }

    public void loadFromFile(String file) throws IOException {
        InputStream input = null;
        try {
            //get an InputStream from our URL
            input = new FileInputStream(file);

            //initialize the decoder
            PNGDecoder dec = new PNGDecoder(input);

            width = dec.getWidth();
            height = dec.getHeight();

            //create a new byte buffer which will hold our pixel data
            ByteBuffer buf = BufferUtils.createByteBuffer(bpp * width * height);

            //decode the image into the byte buffer, in RGBA format
            dec.decode(buf, width * bpp, PNGDecoder.Format.RGBA);

            buffer = buf.asFloatBuffer().array();
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Color getPixel(int x, int y) {
        int offset = x * bpp + y * width * bpp;

        return new Color(buffer[offset], buffer[offset+1], buffer[offset+2], buffer[offset+3]);
    }

    public void setPixel(int x, int y, Color c) {
        int offset = x * bpp + y * width * bpp;

        buffer[offset  ] = c.r;
        buffer[offset+1] = c.g;
        buffer[offset+2] = c.b;
        buffer[offset+3] = c.a;
    }
}
