package System;


import Graphics.Shader;
import OpenGL.GLM;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

/**
 * A camera decides how graphics will be draw by transforming their coordinates according to camera settings/properties.
 * Camera is an interface designed to control RenderTarget Views.
 * @see Viewport
 */
public abstract class Camera {
    public Camera(){}

    public abstract Matrix4f getModelMatrix() ;

    public abstract Matrix4f getViewMatrix() ;

    public abstract Matrix4f getProjectionMatrix() ;

    public final float[] getModelBuffer() {
        return GLM.toFloatArray(getModelMatrix());
    }

    public final float[] getViewBuffer() {
        return GLM.toFloatArray(getViewMatrix());
    }

    public final float[] getProjectionBuffer() {
        return GLM.toFloatArray(getProjectionMatrix());
    }

    /**
     * Sets up MVP matrices to a RenderTarget for Display list
     */
    public abstract void apply() ;

    /**
     * Applies M/V/P Matrices to shader using M/V/P matrices names
     * @param shader target
     * @param model model matrix
     * @param view view matrix
     * @param projection projection matrix
     */
    public final void apply(Shader shader, String model, String view, String projection) {
        this.glUniformMVP(shader, model, view, projection);
    }

    /**
     * Sets up MVP matrix to shader using GLSL matrix name
     * @param shader target
     * @param mvp MVP matrix name
     */
    public final void apply(Shader shader, String mvp) {
        final int MVP = glGetUniformLocation((int)shader.getGlId(), mvp);
        this.glUniformMVP(MVP);
    }

    /**
     * Sets up MVP matrix using GLSL matrix uniform location
     * @param mvpUniform
     */
    public final void glUniformMVP(int mvpUniform) {
        if (mvpUniform < 0) {
            System.out.println("MVP Uniform matrix do not exist in the shader.");
            return;
        }

        GL20.glUniformMatrix4fv(mvpUniform, false, GLM.toFloatArray((Matrix4f.mul(getProjectionMatrix(), Matrix4f.mul(getViewMatrix(), getModelMatrix(), new Matrix4f()), new Matrix4f()))));
    }

    /**
     * Sets up MVP matrices to shader using GLSL matrices names
     * @param shader target
     * @param model
     * @param view
     * @param projection
     */
    public final void glUniformMVP(Shader shader, String model, String view, String projection) {
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
    public final void glUniformMVP(int modelUniform, int viewUniform, int projectionLayout){
        if (modelUniform < 0 || viewUniform < 0 || projectionLayout < 0) {
            System.out.println("MVP Uniform matrix do not exist in the shader.");
            return;
        }

        // send MVP Data
        GL20.glUniformMatrix4fv(modelUniform, false, getModelBuffer());
        GL20.glUniformMatrix4fv(viewUniform, false, getViewBuffer());
        GL20.glUniformMatrix4fv(projectionLayout, false, getProjectionBuffer());
    }

    public static void glUniformMVP(int uniformModel, Matrix4f model, int uniformView, Matrix4f view, int uniformProjection, Matrix4f projection) {
        glUniformMVP(uniformModel, GLM.toFloatArray(model), uniformView, GLM.toFloatArray(view), uniformProjection, GLM.toFloatArray(projection));
    }

    public static void glUniformMVP(int uniformModel, float[] model, int uniformView, float[] view, int uniformProjection, float[] projection) {
        if (uniformModel < 0 || uniformView < 0 || uniformProjection < 0) {
            System.out.println("MVP Uniform matrix do not exist in the shader.");
            return;
        }

        // send MVP Data
        GL20.glUniformMatrix4fv(uniformModel, false, model);
        GL20.glUniformMatrix4fv(uniformView, false, view);
        GL20.glUniformMatrix4fv(uniformProjection, false, projection);
    }
}
