package Graphics;

public abstract class Shape extends Transformable implements Drawable {
    protected Color color = Color.White;

    public void setFillColor(Color color) {
        this.color = color;
    }
}
