package Graphics;


public class Vector2i {
    public int x, y;

    public Vector2i(){
        x = y = 0;
    }

    public Vector2i(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2i(Vector2i v){
        x = v.x;
        y = v.y;
    }

    public Vector2i(Vector2f v) {
        x = (int)v.x;
        y = (int)v.y;
    }

    @Override
    public Vector2i clone() {
        return new Vector2i(x,y);
    }
}
