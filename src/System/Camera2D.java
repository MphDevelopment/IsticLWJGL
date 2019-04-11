package System;

import Graphics.FloatRect;
import Graphics.Vector2f;
import OpenGL.GLM;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Camera2D extends Camera {
    private Vector2f center = new Vector2f(0,0);
    private float zoom = 1;
    private float angle = 0;

    public Camera2D(Vector2f dimension){
        super(dimension);
        center.x = dimension.x / 2 ;
        center.y = dimension.y / 2 ;
    }

    public Camera2D(RenderTarget target){
        this(new Vector2f(target.getDimension()));
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

    private float transformX(float v) {
        float t = (float)(Math.cos(angle) * v);
        return t;
        //return t/zoom;
    }

    private float transformY(float v) {
        float t = (float)(Math.sin(angle) * v);
        return t;
        //return t/zoom;
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

        glViewport(0, 0, (int)screenDimension.x, (int)screenDimension.y);

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
