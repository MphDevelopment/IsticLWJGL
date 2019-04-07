package OpenGL;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class GLM {

    @Deprecated
    public static FloatBuffer toFloatBuffer(Matrix4f m) {
        FloatBuffer buffer = FloatBuffer.allocate(16);
        buffer.put(m.m00);
        buffer.put(m.m01);
        buffer.put(m.m02);
        buffer.put(m.m03);

        buffer.put(m.m10);
        buffer.put(m.m11);
        buffer.put(m.m12);
        buffer.put(m.m13);

        buffer.put(m.m20);
        buffer.put(m.m21);
        buffer.put(m.m22);
        buffer.put(m.m23);

        buffer.put(m.m20);
        buffer.put(m.m21);
        buffer.put(m.m22);
        buffer.put(m.m23);

        buffer.flip();

        return buffer;
    }
    public static float[] toFloatArray(Matrix4f m) {
        return new float[]{
                m.m00,
                m.m01,
                m.m02,
                m.m03,

                m.m10,
                m.m11,
                m.m12,
                m.m13,

                m.m20,
                m.m21,
                m.m22,
                m.m23,

                m.m30,
                m.m31,
                m.m32,
                m.m33,
        };
    }


    public static Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar) {

        Matrix4f m = new Matrix4f();

        m.m00 = 2 / (right - left);
        m.m11 = 2 / (top - bottom);
        m.m22 = - 2 / (zFar - zNear);
        m.m30 = - (right + left) / (right - left);
        m.m31 = - (top + bottom) / (top - bottom);
        m.m32 = - (zFar + zNear) / (zFar - zNear);

        return m;
    }

    public static Matrix4f frustum(float left, float right, float bottom, float top, float zNear, float zFar) {

        Matrix4f m = new Matrix4f();

        m.m00 = (2 * zNear) / (right - left);
        m.m11 = (2 * zNear) / (top - bottom);
        m.m20 = (right + left) / (right - left);
        m.m21 = (top + bottom) / (top - bottom);
        m.m22 = -(zFar + zNear) / (zFar - zNear);
        m.m23 = -1;
        m.m32 = -(2 * zFar * zNear) / (zFar - zNear);

        return m;
    }

    public static Matrix4f perspective(float fovy, float aspect, float zNear, float zFar) {

        float range = (float) Math.tan(Math.toRadians(fovy / 2)) * zNear;
        float left = -range * aspect;
        float right = range * aspect;
        float bottom = -range;
        float top = range;

        Matrix4f m = new Matrix4f();

        m.m00 = (2 * zNear) / (right - left);
        m.m11 = (2 * zNear) / (top - bottom);
        m.m22 = - (zFar + zNear) / (zFar - zNear);
        m.m23 = - 1;
        m.m32 = - (2 * zFar * zNear) / (zFar - zNear);

        return m;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {

        Vector3f forward = Vector3f.sub(center, eye, null);
        forward.normalise(forward);

        Vector3f u = up.normalise(null);

        Vector3f side = Vector3f.cross(forward, u, null);
        side.normalise(side);

        Vector3f.cross(side, forward, u);

        Matrix4f m = new Matrix4f();

        m.m00 = side.x;
        m.m10 = side.y;
        m.m20 = side.z;

        m.m01 = u.x;
        m.m11 = u.y;
        m.m21 = u.z;

        m.m02 = -forward.x;
        m.m12 = -forward.y;
        m.m22 = -forward.z;

        m.translate(new Vector3f(-eye.x, -eye.y, -eye.z));

        return m;
    }
}