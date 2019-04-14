package Graphics;


public final class Color {
    public float r,g,b,a;

    /**
     * Generating OpenGL Color
     * @param r Red value [must be between 0 and 1]
     * @param g Green value [must be between 0 and 1]
     * @param b Blue value [must be between 0 and 1]
     *          Alpha default value is 1
     */
    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
    }

    /**
     * Generating OpenGL Color
     * @param r Red value [must be between 0 and 1]
     * @param g Green value [must be between 0 and 1]
     * @param b Blue value [must be between 0 and 1]
     * @param a Opacity value [must be between 0 and 1]
     */
    public Color(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    // Default colors
    public static final Color Blue = new Color(0,0,1,1);
    public static final Color Green = new Color(0,1,0,1);
    public static final Color Red = new Color(1,0,0,1);
    public static final Color Black = new Color(0,0,0,1);
    public static final Color White = new Color(1,1,1,1);
    public static final Color Yellow = new Color(1,1,0,1);
    public static final Color Magenta = new Color(1,0,1,1);
    public static final Color Cyan = new Color(0,1,1,1);
    public static final Color Transparent = new Color(0,0,0,0);


}
