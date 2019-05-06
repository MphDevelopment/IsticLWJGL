package Graphics;

/**
 * Shape can be drawn by GPU and can be transformed.
 * Only the window activated during the Shape creation can draw the shape. (Because VBO can't be shared)
 * To share to a specific window you must activate it before.
 */
public abstract class Shape extends Transformable implements Drawable {
    protected Color color = new Color(1,1,1,1);
    protected float width = 0, height = 0;

    protected VertexBuffer buffer;

    /**
     * Applies new color to the graphic component
     * @param color fill color
     */
    public void setFillColor(ConstColor color) {
        this.color = new Color(color.getR(), color.getG(), color.getB(), color.getA());
        updateColor();
    }


    protected void updateColor(){
        if (buffer != null) buffer.update(1, new float[]{color.r,color.g,color.b,color.a, color.r,color.g,color.b,color.a, color.r,color.g,color.b,color.a, color.r,color.g,color.b,color.a});
    }


    /**
     * Release shape GPU resources
     */
    public final void delete(){
        if (buffer == null) return ;

        buffer.free();
        buffer = null;
    }

    /**
     * Global bounds of the shape
     * @return bounds
     */
    public abstract FloatRect getBounds() ;

}
