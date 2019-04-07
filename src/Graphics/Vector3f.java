package Graphics;

/**
 * Represents a 3D point/vector/dimension
 */
public class Vector3f implements Comparable<Vector3f> {
    public float x;
    public float y;
    public float z;

    /**
     * Generates a vector placed at (x,y,z)
     * @param x x coordinates
     * @param y y coordinates
     * @param z z coordinates
     */
    public Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Generates a vector placed at (v.x,v.y,v.z)
     * @param v copied vector
     */
    public Vector3f(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     * Default constructor.
     * Generate a vector placed at (0,0,0)
     */
    public Vector3f(){
        this.x = this.y = this.z = 0;
    }

    // self-operation

    /**
     * Translates the coordinates
     * @param vec3 added coordinates
     * @return this
     */
    public Vector3f add(Vector3f vec3) {
        this.x += vec3.x;
        this.y += vec3.y;
        this.z += vec3.z;
        return this;
    }

    /**
     * Multiplies the coordinates by a factor
     * @param f factor
     * @return this
     */
    public Vector3f fact(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    /**
     * The vector will be normalized.
     * Means that the length will be equals to 1.
     * @requires coordinates must be different to (0,0,0)
     * @return this
     */
    public Vector3f normalize() {
        double l = this.length();
        this.x /= l;
        this.y /= l;
        this.z /= l;
        return this;
    }

    /**
     * The vector will be inverted
     * @return this
     */
    public Vector3f negate(){
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    /**
     * Builds an inverted vector using negative 'this' coordinates
     * @return inverted vector
     */
    public Vector3f neg(){
        return new Vector3f(-this.x, -this.y, -this.z);
    }

    /**
     * Builds a vector with a norm 'f' times longer
     * @param f specified factor
     * @return a vector with a norm 'f' times longer
     */
    public Vector3f mul(float f) {
        return new Vector3f(this.x * f, this.y * f, this.z * f);
    }

    /**
     * Builds a new translated vector
     * @param v3 added coordinates
     * @return sum of two vectors
     */
    public Vector3f sum(Vector3f v3){
        Vector3f vt = new Vector3f(this.x, this.y, this.z);
        vt.x += v3.x;
        vt.y += v3.y;
        vt.z += v3.z;
        return vt;
    }

    /**
     * Builds a normalized vector using 'this' coordinates
     * Means that the length of the returned vector will be equals to 1.
     * @requires coordinates must be different to (0,0,0)
     * @return normalized vector
     */
    public Vector3f unit() {
        double l = this.length();
        return new Vector3f((float)(this.x / l), (float)(this.y / l), (float)(this.z / l));
    }

    /**
     * Computes the squared length of 'this'
     * @return squared length of 'this'
     */
    public double slength() {
        return x*x+y*y+z*z;
    }

    /**
     * Computes the length of 'this'
     * @return length of the vector
     */
    public double length(){
        return Math.sqrt(x*x+y*y+z*z);
    }

    /**
     * Computes the scalar of two vectors
     * @param v1
     * @param v2
     * @return
     */
    public static float scalar(Vector3f v1, Vector3f v2){
        return v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
    }

    /**
     * Computes the normal of two distinct vectors
     * @param v1
     * @param v2
     * @return
     */
    public static Vector3f product(Vector3f v1, Vector3f v2) {
        Vector3f vt = new Vector3f((v1.y * v2.z) - (v1.z * v2.y), (v1.z * v2.x) - (v1.x * v2.z), (v1.x * v2.y) - (v1.y * v2.x));
        vt.negate();
        return vt;
    }


    /**
     * Converts to native lwjgl vector
     * @return
     */
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
