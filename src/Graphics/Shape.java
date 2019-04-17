package Graphics;

/**
 * Shape can be drawn by GPU and can be transformed
 */
public abstract class Shape extends Transformable implements Drawable {
    protected Color color = Color.White;
    protected float width = 0, height = 0;

    /**
     * Applies new color to the graphic component
     * @param color fill color
     */
    public void setFillColor(Color color) {
        this.color = color;
    }

    /**
     * Global bounds of the shape
     * @return bounds
     */
    public abstract FloatRect getBounds() ;

}
