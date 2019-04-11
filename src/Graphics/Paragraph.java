package Graphics;


import java.util.ArrayList;

public class Paragraph extends Shape {
    private float maxx, maxy;
    private ArrayList<Text> lines;

    public Vector2i add(int character, int x, int y) {
        return null;
    }

    public void removeLine(int y) {}

    public void addLine(int y) {}

    public int length() {
        return lines.size();
    }

    public String getString(){
        return null;
    }

    public String[] getStrings() {
        return new String[0];
    }

    public void draw(){}
}
