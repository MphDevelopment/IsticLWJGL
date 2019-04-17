package Graphics;


import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;

/**
 * Image represents a 2D graphic RGBA8 image that can be edit by the CPU.
 * Contrary to Texture it can not be sent to the GPU to be drawn.
 * Contrary to Texture, data are saved into the RAM.
 * @see Texture Texture (DRAM only)
 */
public class Image {
    private static final int bpp = 4;

    private int width;
    private int height;
    private byte[] buffer;

    /**
     * Create a default image with no pixels that needs to be initialized after.
     */
    public Image(){
        width = 0;
        height = 0;
        buffer = null;
    }

    /**
     * Create a default image with default dimension w*h with white background color.
     * @param w image width
     * @param h image height
     */
    public Image(int w, int h) {
        this.create(w,h);
    }

    /**
     * Change current image's dimension with dimension w*h with white background color.
     * @param w image width.
     * @param h image height.
     */
    public void create(int w, int h) {
        width = w;
        height = h;

        buffer = new byte[w*h*bpp];
        for (int i=0 ; i < buffer.length ; ++i) {
            buffer[i] = (byte)255;
        }
    }

    /**
     * Creates an image from a byte buffer.
     * @param buffer byte buffer with 4 component RGBA for each pixel and with [0 to 255] range value for each components. The byte buffer must contains w * h * 4 bytes.
     * @param w image width.
     * @param h image height.
     */
    public Image(ByteBuffer buffer, int w, int h) {
        width = w;
        height = h;

        this.buffer = new byte[w*h*bpp];
        buffer.get(this.buffer);
    }

    /**
     * Creates an image from a byte array.
     * @param buffer byte array with 4 component RGBA for each pixel and with [0 to 255] range value for each components. The byte buffer must contains w * h * 4 bytes.
     * @param w image width.
     * @param h image height.
     */
    public Image(byte[] buffer, int w, int h) {
        width = w;
        height = h;

        this.buffer = buffer;
    }

    /**
     * Create an image loaded from a PNG file.
     * @param file specified file
     * @throws IOException thrown when image can't be load from file
     */
    public Image(String file) throws IOException {
        this.loadFromFile(file);
    }


    /**
     * Loads current image from a PNG file.
     * @param file specified file
     * @throws IOException thrown when image can't be load from file
     */
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
            ByteBuffer buf = ByteBuffer.allocate(bpp * width * height);

            //decode the image into the byte buffer, in RGBA format
            dec.decode(buf, width * bpp, PNGDecoder.Format.RGBA);

            buf.flip();

            buffer = buf.array();
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

    /**
     * Gives the color of a given pixel at coordinates [x,y].
     * @param x x-coordinates.
     * @param y y-coordinates.
     * @return converted pixel in [0-255] range to [0-1]
     */
    public Color getPixel(int x, int y) {
        int offset = x * bpp + y * width * bpp;

        return new Color(buffer[offset]/255.f, buffer[offset+1]/255.f, buffer[offset+2]/255.f, buffer[offset+3]/255.f);
    }

    /**
     * Sets the color of a given pixel at coordinates [x,y]
     * @param x x-coordinates.
     * @param y y-coordinates.
     * @param c openGL color
     */
    public void setPixel(int x, int y, Color c) {
        int offset = x * bpp + y * width * bpp;

        // openGL color colors are in range [0-1] and must be converted to [0-255]
        buffer[offset  ] = (byte)(c.r*255);
        buffer[offset+1] = (byte)(c.g*255);
        buffer[offset+2] = (byte)(c.b*255);
        buffer[offset+3] = (byte)(c.a*255);
    }

    /**
     * Creates a PNG file using a specific name.
     * @param filename file name without extension.
     * @throws IOException thrown when image save did not correctly end up
     */
    public void saveAs(String filename) throws IOException {
        DataBufferByte dbb = new DataBufferByte(buffer, buffer.length);
        WritableRaster raster = Raster.createInterleavedRaster(
                dbb,
                width, height,
                width * bpp,
                bpp,
                new int []{ 0, 1, 2, 3},
                null);

        ColorModel colorModel =  new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] { 8, 8, 8, 8},
                true,
                false,
                Transparency.OPAQUE,
                DataBuffer.TYPE_BYTE);

        BufferedImage bfImage = new BufferedImage(colorModel, raster, false, null);

        ImageIO.write(bfImage, "png", new File(filename+".png"));
    }

    public static void main(String[] args) {
        Image image = new Image();

        image.create(50,50);

        for (int i=0 ; i < 50 ; ++i) {
            for (int j=0 ; j < 50 ; j++) {
                image.setPixel(i,j, new Color(0.f, 1.f, 1.f, (i+j)/(100.f)));
            }
        }

        try {
            //image.loadFromFile("perf.png");
            image.saveAs("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
