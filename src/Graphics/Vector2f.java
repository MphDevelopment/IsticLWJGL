package Graphics;


/**
 * Represents a 2D point/vector/dimension.
 */
public class Vector2f {
    public float x;
    public float y;

    public Vector2f(){
        this.x = this.y = 0;
    }

    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v) {
        x = v.x;
        y = v.y;
    }

    public Vector2f(Vector2i v) {
        x = v.x;
        y = v.y;
    }

    // self-operation
    public Vector2f add(Vector2f vec2) {
        this.x += vec2.x;
        this.y += vec2.y;
        return this;
    }

    public Vector2f fact(float f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public Vector2f normalize() {
        double l = this.length();
        this.x /= l;
        this.y /= l;
        return this;
    }

    public Vector2f negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    //
    public Vector2f neg(){
        return new Vector2f(-this.x, -this.y);
    }

    public Vector2f mul(float f) {
        return new Vector2f(this.x * f, this.y * f);
    }

    public Vector2f sum(Vector2f v2){
        Vector2f vt = new Vector2f(this.x, this.y);
        vt.x += v2.x;
        vt.y += v2.y;
        return vt;
    }

    public Vector2f unit() {
        double l = this.length();
        return new Vector2f((float)(this.x / l), (float)(this.y / l));
    }

    public double slength() {
        return x*x+y*y;
    }

    public double length(){
        return Math.sqrt(x*x+y*y);
    }

    @Override
    public Vector2f clone() {
        return new Vector2f(x,y);
    }
    /*public static float scalar(Vector2f v1, Vector2f v2){
        return v1.x*v2.x+v1.y*v2.y;
    }

    public static Vector3f product(Vector2f v1, Vector2f v2) {
        Vector3f vt = new Vector3f((v1.y * v2.z) - (v1.z * v2.y), (v1.z * v2.x) - (v1.x * v2.z), (v1.x * v2.y) - (v1.y * v2.x));
        vt.negate();
        return vt;
    }*/

}
