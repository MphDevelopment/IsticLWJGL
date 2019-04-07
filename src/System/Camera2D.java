package System;

import Graphics.Vector2f;
import OpenGL.GLM;
import org.lwjgl.util.vector.Matrix4f;

public class Camera2D {
    private Vector2f _topleft = new Vector2f(0,0);
    private Vector2f _dimension = new Vector2f(0,0);

    public Camera2D(Vector2f dimension){
        _dimension = dimension;
    }

    public Camera2D(GLFWWindow window){
        _dimension = window.getDimension().clone();
    }

    public Vector2f getTopLeftCorner() {
        return _topleft;
    }

    public Vector2f getDimension() {
        return _dimension;
    }

    public void moveView(Vector2f motion) {
        _topleft.add(motion);
    }

    public void setTopLeftCorner(Vector2f position) {
        _topleft = position;
    }

    public void setDimension(Vector2f dimension) {
        _dimension = dimension;
    }

    public Matrix4f getOrthoMatrix() {
        return GLM.ortho(_topleft.x,_dimension.x,_dimension.y, _topleft.y, -1, 1);
    }

    public float[] getOrthoBuffer() {
        return GLM.toFloatArray(this.getOrthoMatrix());
    }

}
