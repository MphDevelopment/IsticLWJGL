package System;

import Graphics.FloatRect;
import Graphics.Vector2f;
import OpenGL.GLM;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

//TODO orthogonal matrices must concider zoom and angle
public class Camera2D extends Camera {
    private Vector2f screenDimension;
    private Vector2f center;
    private float zoom = 1;
    private float angle = 0;

    public Camera2D(Vector2f dimension){
        screenDimension = dimension;
        center = new Vector2f(dimension.x / 2,dimension.y / 2);
    }

    public Camera2D(RenderTarget target){
        this(new Vector2f(target.getDimension()));
    }

    public void setDimension(Vector2f dimension) {
        this.screenDimension.x = dimension.x;
        this.screenDimension.y = dimension.y;
    }

    public Vector2f getDimension() {
        return new Vector2f(screenDimension.x, screenDimension.y);
    }

    @Override
    public Matrix4f getModelMatrix() {
        return (Matrix4f) new Matrix4f().setIdentity();
        //return new Matrix4f().translate(new Vector3f(viewport.l,viewport.t,0));
    }
    @Override
    public Matrix4f getViewMatrix() {
        //return (Matrix4f) new Matrix4f().setIdentity();
        return new Matrix4f()
                //.translate(new org.lwjgl.util.vector.Vector2f(zoom*-viewport.l/(viewport.w*viewport.w),zoom*-viewport.t/(viewport.h*viewport.h)))
                //.translate(new org.lwjgl.util.vector.Vector2f(-viewport.l,-viewport.t))
                .scale(new Vector3f(zoom,zoom,1))
                .rotate(angle, new Vector3f(0,0,1));
    }
    @Override
    public Matrix4f getProjectionMatrix() {
        return GLM.ortho(center.x - screenDimension.x/2, center.x + screenDimension.x/2, center.y + screenDimension.y/2, center.y - screenDimension.y/2, -1f, 1.f);
    }

    public void move(Vector2f motion) {
        center.x += motion.x;
        center.y += motion.y;
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public void zoom(float zoom) {
        this.zoom += zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    /**
     *
     * @param angle radian
     */
    public void setRotation(float angle) {
        this.angle = angle;
    }

    /**
     *
     * @param angle radian
     */
    public void rotate(float angle) {
        this.angle += angle;
    }


    @Override
    public void apply(RenderTarget target) {
        glDisable(GL_DEPTH_TEST);

        //glViewport(0, 0, (int)screenDimension.x, (int)screenDimension.y);

        /// change fov
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glLoadMatrixf(getProjectionBuffer());

        /// change position
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glLoadMatrixf(getViewBuffer());

    }
}
