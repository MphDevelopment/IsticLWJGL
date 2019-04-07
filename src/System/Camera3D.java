package System;


import Graphics.Shader;
import Graphics.Vector3f;
import OpenGL.GLM;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class Camera3D {
    protected Vector3f _center = new Vector3f(0,0,0);
    protected Vector3f _up = new Vector3f(0,1,0);
    protected Vector3f _eye  = new Vector3f(0,0,1);
    protected float _fov = 90.f;
    protected float _znear = 1;
    protected float _zfar = 1000;
    protected float _aspectRatio = 3.f/4.f;

    /**
     * Generates Camera with default settings
     * center = (0,0,0)
     * eye = (0,0,1)
     * up = (0,1,0)
     * fov = (90)
     * znear = 1;
     * zfar = 1000;
     * aspectRatio = 3/4
     */
    public Camera3D(){}

    /**
     * Generates Camera using specific settings
     * @param fov field of view in degree
     * @param znear near depth buffer limit
     * @param zfar far depth buffer limit
     * @param window for view aspect
     */
    public Camera3D(float fov, float znear, float zfar, GLFWWindow window) {
        this._fov = fov;
        this._znear = znear;
        this._zfar = zfar;
        this._aspectRatio = window.getDimension().x / window.getDimension().y;
    }

    /**
     * Generates Camera using specific settings
     * @param fov field of view in degree
     * @param znear near depth buffer limit
     * @param zfar far depth buffer limit
     * @param aspect view aspect
     */
    public Camera3D(float fov, float znear, float zfar, float aspect){
        this._fov = fov;
        this._znear = znear;
        this._zfar = zfar;
        this._aspectRatio = aspect;
    }

    public float getFOV() {
        return _fov;
    }

    public float getAspectRatio() {
        return _aspectRatio;
    }

    public float getZNear() {
        return _znear;
    }

    public float getZFar() {
        return _zfar;
    }

    public Vector3f getOrientation() {
        return new Vector3f(_eye);
    }

    public Vector3f getCenter() {
        return new Vector3f(_center);
    }

    public Vector3f getUpVector()  {
        return new Vector3f(_up);
    }

    public void setFOV(float fov) {
        this._fov = fov;
    }

    public void setAspectRatio(float aspect) {
        this._aspectRatio = aspect;
    }

    public void getZNear(float znear) {
        _znear = znear;
    }

    public void getZFar(float zfar) {
        _zfar = zfar;
    }

    public void move(Vector3f motion) {
        _center.add(motion);
    }

    public void setPosition(Vector3f position) {
        _center = position;
    }

    /**
     * Sets the orientation of the camera where it look to
     * @see Camera3D#lookAt(Vector3f)
     * @param eye
     */
    public void look(Vector3f eye) {
        _eye = eye.unit();
    }

    /**
     * Sets the position where the camera look to
     * @see Camera3D#look(Vector3f)
     * @param target
     */
    public void lookAt(Vector3f target) {
        _eye = target.sum(_center.neg()).unit();
        //System.out.println(_eye.x + ", " + _eye.y + ", " + _eye.z);
    }

    public void setUpVector(Vector3f up) {
        _up = up.unit();
    }

    /**
     * Sets up MVP matrice to a GLFWWindow
     * @param window
     */
    public void apply(GLFWWindow window) {
        if (!window.isOpen()) return;

        glEnable(GL_DEPTH_TEST);

        /// change fov
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        Matrix4f perspect = GLM.perspective(_fov, _aspectRatio, _znear, _zfar);
        glLoadMatrixf(GLM.toFloatArray(perspect));

        /// change position
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        Matrix4f look = GLM.lookAt(_center.toLwjgl(), _eye.sum(_center).toLwjgl(), _up.toLwjgl());
        glLoadMatrixf(GLM.toFloatArray(look));
    }

    /**
    * Applies M/V/P Matrices to shader
    * @param shader target
    * @param model model matrix
    * @param view view matrix
    * @param projection projection matrix
    */
    public void apply(Shader shader, String model, String view, String projection) {
        this.glUniformMVP(shader, model, view, projection);
    }

    /**
     * Sets up MVP matrix using GLSL matrix name
     * @param shader target
     * @param mvp MVP matrix name
     */
    public void glUniformMVP(Shader shader, String mvp) {
        final int MVP = glGetUniformLocation((int)shader.getGlId(), mvp);
        this.glUniformMVP(MVP);
    }

    /**
     * Sets up MVP matrix using GLSL matrix uniform location
     * @param mvpUniform
     */
    public void glUniformMVP(int mvpUniform) {
        if (mvpUniform < 0) {
            System.out.println("MVP Uniform matrix do not exist in the shader.");
            return;
        }

        GL20.glUniformMatrix4fv(mvpUniform, false, GLM.toFloatArray((Matrix4f.mul(getProjectionMatrix(), Matrix4f.mul(getViewMatrix(), getModelMatrix(), new Matrix4f()), new Matrix4f()))));
    }

    /**
     * Sets up MVP matrices using GLSL matrices names
     * @param shader target
     * @param model
     * @param view
     * @param projection
     */
    public void glUniformMVP(Shader shader, String model, String view, String projection) {
        final int M = glGetUniformLocation((int)shader.getGlId(), model);
        final int V = glGetUniformLocation((int)shader.getGlId(), view);
        final int P = glGetUniformLocation((int)shader.getGlId(), projection);
        this.glUniformMVP(M,V,P);
    }

    /**
     * Sets MVP matrices using GLSL matrices uniform locations
     * @param modelUniform
     * @param viewUniform
     * @param projectionLayout
     */
    public void glUniformMVP(int modelUniform, int viewUniform, int projectionLayout){
        if (modelUniform < 0 || viewUniform < 0 || projectionLayout < 0) {
            System.out.println("MVP Uniform matrix do not exist in the shader.");
            return;
        }

        // send MVP Data
        GL20.glUniformMatrix4fv(modelUniform, false, getModelBuffer());
        GL20.glUniformMatrix4fv(viewUniform, false, getViewBuffer());
        GL20.glUniformMatrix4fv(projectionLayout, false, getProjectionBuffer());
    }

    public Matrix4f getModelMatrix() {
        return (Matrix4f) new Matrix4f().setIdentity();
    }

    public Matrix4f getViewMatrix() {
        return GLM.lookAt(_center.toLwjgl(), _eye.sum(_center).toLwjgl(),  _up.toLwjgl());
    }

    public Matrix4f getProjectionMatrix() {
        return GLM.perspective(/*(float)Math.toRadians(_fov)*/_fov, _aspectRatio, _znear, _zfar);
    }

    public float[] getModelBuffer() {
        return GLM.toFloatArray(getModelMatrix());
    }

    public float[] getViewBuffer() {
        return GLM.toFloatArray(getViewMatrix());
    }

    public float[] getProjectionBuffer() {
        return GLM.toFloatArray(getProjectionMatrix());
    }

    public static void glUniformMVP(int uniformModel, Matrix4f model, int uniformView, Matrix4f view, int uniformProjection, Matrix4f projection) {

    }

}
