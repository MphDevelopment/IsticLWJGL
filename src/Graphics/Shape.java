package Graphics;

public abstract class Shape extends Transformable implements Drawable {
    protected Color color = Color.White;

    /**
     * Applies new color to the graphic component
     * @param color fill color
     */
    public void setFillColor(Color color) {
        this.color = color;
    }
}
