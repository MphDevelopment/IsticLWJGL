package Graphics;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

/**
 * "Display list" Text
 */
public class Text extends Shape {
    public static final int REGULAR = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;

    private float px;
    private int style;
    private String string = "";
    private FontFamily font = null;

    public Text(FontFamily font, String str) {
        this(font, str, REGULAR);
    }

    public Text(FontFamily font, String str, int styles) {
        this.px = font.getFontImageHeight();

        this.font = font;
        this.string = str;
        this.style = styles;

        update();
    }

    public void setString(String content) {
        this.string = content;

        update();
    }

    @Override
    protected void update() {
        this.resize();
    }

    private void resize() {
        float xTmp = x;
        float charHeight = font.getCharHeight();

        float italicOffset = ((style & ITALIC) == ITALIC) ? (this.px / 10.f) : 0.f;
        float boldOffsetX = ((style & BOLD) == BOLD) ? (charHeight / 3.f) : 0.f;
        float boldOffsetY = ((style & BOLD) == BOLD) ? (charHeight / 100.f) : 0.f;


        for (char c : string.toCharArray()) {
            float width = font.getCharWidth(c);

            xTmp += width * super.sx + boldOffsetX;
        }

        width = xTmp - x + italicOffset;
        height = charHeight * super.sy + boldOffsetY;
    }

    public String getString() {
        return string;
    }

    @Override
    public void draw() {
        glBindTexture(GL_TEXTURE_2D, (int)font.getGlId());
        glBegin(GL_QUADS);

        float xTmp = x;
        float height = font.getCharHeight();
        float italicOffset = ((style & ITALIC) == ITALIC) ? (this.px / 10.f) : 0.f;
        float boldOffsetX = ((style & BOLD) == BOLD) ? (height / 3.f) : 0.f;
        float boldOffsetY = ((style & BOLD) == BOLD) ? (height / 100.f) : 0.f;

        for (char c : string.toCharArray()) {
            float width = font.getCharWidth(c);

            float cw = 1f / font.getFontImageWidth() * width;
            float ch = 1f / font.getFontImageHeight() * height;
            float cx = 1f / font.getFontImageWidth() * font.getCharX(c);
            float cy = 1f / font.getFontImageHeight() * font.getCharY(c);

            glColor3f(color.r, color.g, color.b);

            glTexCoord2f(cx, cy);
            glVertex3f(xTmp + ox + italicOffset, y + oy, 0);

            glTexCoord2f(cx + cw, cy);
            glVertex3f(xTmp + width * super.sx + ox + italicOffset + boldOffsetX, y + oy, 0);

            glTexCoord2f(cx + cw, cy + ch);
            glVertex3f(xTmp + width * super.sx + ox + boldOffsetX, y + height * super.sy + oy + boldOffsetY, 0);

            glTexCoord2f(cx, cy + ch);
            glVertex3f(xTmp + ox , y + height * super.sy + oy + boldOffsetY, 0);

            xTmp += width * super.sx + boldOffsetX;
        }
        glEnd();

        glBindTexture(GL_TEXTURE_2D,0);
    }

    @Override
    public FloatRect getBounds() {
        return new FloatRect(x - ox, y - oy, width, height);
    }

}
