package System;


import Graphics.FloatRect;
import Graphics.Shader;
import Graphics.Vector2f;
import Graphics.Vector3f;
import OpenGL.GLM;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

/**
 * Camera3D is an interface designed to control RenderTarget Views.
 * Camera3D enables GL_DEPTH_TEST.
 * @see Camera2D
 */
public class Camera3D extends Camera {

    private Vector3f _center = new Vector3f(0,0,0);
    private Vector3f _up = new Vector3f(0,1,0);
    private Vector3f _eye  = new Vector3f(0,0,1);
    private float _fov = 90.f;
    private float _znear = 1;
    private float _zfar = 1000;
    private float _aspectRatio = 3.f/4.f;

    /**
     * Generates Camera with default settings :
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
        this._aspectRatio = (float)window.getDimension().x / (float)window.getDimension().y;
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

    @Override
    public Matrix4f getModelMatrix() {
        return (Matrix4f) new Matrix4f().setIdentity();
    }
    @Override
    public Matrix4f getViewMatrix() {
        return GLM.lookAt(_center/*.toLwjgl()*/, _eye.sum(_center)/*.toLwjgl()*/,  _up/*.toLwjgl()*/);
    }
    @Override
    public Matrix4f getProjectionMatrix() {
        return GLM.perspective(/*(float)Math.toRadians(_fov)*/_fov, _aspectRatio, _znear, _zfar);
    }

    public void setFOV(float fov) {
        this._fov = fov;
    }

    public void setAspectRatio(float aspect) {
        this._aspectRatio = aspect;
    }

    public void setZNear(float znear) {
        _znear = znear;
    }

    public void setZFar(float zfar) {
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

    /**
     * Sets the direction where the camera's top is
     * @param up
     */
    public void setUpVector(Vector3f up) {
        _up = up.unit();
    }

    @Override
    public void apply() {
        //target.bind();

        glEnable(GL_DEPTH_TEST);

        /// change fov
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(getProjectionBuffer());

        /// change position
        glMatrixMode(GL_MODELVIEW);
        glLoadMatrixf(getViewBuffer());
    }






}
