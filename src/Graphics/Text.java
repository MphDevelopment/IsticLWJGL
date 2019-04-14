package Graphics;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Text extends Shape {
    public static final int REGULAR = 0;
    @Deprecated
    public static final int BOLD = 1;
    public static final int ITALIC = 2;

    private int px;
    private int style;
    private String string = "";
    private FontFamily font = null;

    public Text(FontFamily font, String str) {
        this.font = font;
        this.string = str;
        this.style = REGULAR;
        //this(font, str, REGULAR);
    }

    public Text(FontFamily font, String str, int styles) {
        this.font = font;
        this.string = str;
        this.style = styles;
    }

    public String getString() {
        return string;
    }

    @Override
    public void draw() {
        glBindTexture(GL_TEXTURE_2D, (int)font.getGlId());
        glBegin(GL_QUADS);

        float xTmp = x;
        float italicOffset = ((style & ITALIC) == ITALIC) ? 10.f : 0.f;
        for (char c : string.toCharArray()) {
            float width = font.getCharWidth(c);
            float height = font.getCharHeight();
            float cw = 1f / font.getFontImageWidth() * width;
            float ch = 1f / font.getFontImageHeight() * height;
            float cx = 1f / font.getFontImageWidth() * font.getCharX(c);
            float cy = 1f / font.getFontImageHeight() * font.getCharY(c);

            glColor3f(color.r, color.g, color.b);

            glTexCoord2f(cx, cy);
            glVertex3f(xTmp + ox + italicOffset, y + oy, 0);

            glTexCoord2f(cx + cw, cy);
            glVertex3f(xTmp + width * super.sx + ox + italicOffset, y + oy, 0);

            glTexCoord2f(cx + cw, cy + ch);
            glVertex3f(xTmp + width * super.sx + ox, y + height * super.sy + oy, 0);

            glTexCoord2f(cx, cy + ch);
            glVertex3f(xTmp + ox , y + height * super.sy + oy, 0);

            xTmp += width * super.sx;
        }
        glEnd();

        glBindTexture(GL_TEXTURE_2D,0);
    }

    /*public Vector2f getRectangle() {

    }*/

}
