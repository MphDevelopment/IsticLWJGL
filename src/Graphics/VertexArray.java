package Graphics;


public class VertexArray implements Drawable {
    private float[] positions;
    private float[] colors;
    private float[] coords;

    public VertexArray(int count) {
        positions = new float[count * 3];
        colors = new float[count * 4];
        coords = new float[count * 2];
    }

    public int getVerticesCount() {
        return 0;
    }

    public void setVertexPosition() {}
    public void setVertexColor() {}
    public void setVertexTexCoord() {}

    public void draw() {

    }
}
