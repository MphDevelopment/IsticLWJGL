package Graphics;


public class Vector2i implements Comparable<Vector2i> {
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

    @Override
    public boolean equals(Object v) {
        if (v == this) return true;
        if (v instanceof Vector2i) {
            Vector2i v2 = (Vector2i)v;
            return v2.x == x && v2.y == y;
        } else return false;
    }

    @Override
    public int compareTo(Vector2i o) {
        if (o.x < this.x) return 1;
        else if (o.x > this.x) return -1;
        else {
            if (o.y < this.y) return 1;
            else if (o.y > this.y) return -1;
            else {
                return 0;
            }
        }
    }
}
