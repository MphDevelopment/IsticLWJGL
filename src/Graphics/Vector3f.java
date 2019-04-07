package Graphics;


public class Vector3f implements Comparable<Vector3f> {
    public float x;
    public float y;
    public float z;

    public Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3f(){
        this.x = this.y = this.z = 0;
    }

    // self-operation
    public Vector3f add(Vector3f vec3) {
        this.x += vec3.x;
        this.y += vec3.y;
        this.z += vec3.z;
        return this;
    }

    public Vector3f fact(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    public Vector3f normalize() {
        double l = this.length();
        this.x /= l;
        this.y /= l;
        this.z /= l;
        return this;
    }

    public Vector3f negate(){
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    ///
    public Vector3f neg(){
        return new Vector3f(-this.x, -this.y, -this.z);
    }

    public Vector3f mul(float f) {
        return new Vector3f(this.x * f, this.y * f, this.z * f);
    }

    public Vector3f sum(Vector3f v2){
        Vector3f vt = new Vector3f(this.x, this.y, this.z);
        vt.x += v2.x;
        vt.y += v2.y;
        vt.z += v2.z;
        return vt;
    }

    public Vector3f unit() {
        double l = this.length();
        return new Vector3f((float)(this.x / l), (float)(this.y / l), (float)(this.z / l));
    }

    public double slength() {
        return x*x+y*y+z*z;
    }

    public double length(){
        return Math.sqrt(x*x+y*y+z*z);
    }

    public static float scalar(Vector3f v1, Vector3f v2){
        return v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
    }

    public static Vector3f product(Vector3f v1, Vector3f v2) {
        Vector3f vt = new Vector3f((v1.y * v2.z) - (v1.z * v2.y), (v1.z * v2.x) - (v1.x * v2.z), (v1.x * v2.y) - (v1.y * v2.x));
        vt.negate();
        return vt;
    }

    public org.lwjgl.util.vector.Vector3f toLwjgl() {
        return new org.lwjgl.util.vector.Vector3f(x,y,z);
    }

    @Override
    public int compareTo(Vector3f o) {
        if (o.x == this.x && o.y == this.y && o.z == this.z)
            return 0;
        else return 1;
    }
}
