package System;

import Graphics.FloatRect;
import Graphics.Vector2f;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * A viewport defines an area on a RenderTarget where the current display will be drawn, contrary to a Camera that define how to draw graphics on a current Viewport.
 * A camera needs a viewport and a viewport needs a RenderTarget.
 * @see Camera
 */
public class Viewport {
    private Vector2f targetDimension;
    private FloatRect rect;

    public Viewport(FloatRect rect, float targetDimension){
        this.targetDimension = new Vector2f(0, targetDimension);
        this.rect = rect;
    }

    public Viewport(FloatRect rect, RenderTarget target){
        this.targetDimension = new Vector2f(0, target.getDimension().y);
        this.rect = rect;
    }

    public void update(RenderTarget target) {
        this.targetDimension.y = target.getDimension().y;
    }

    public Vector2f getTopleftCorner(){
        return new Vector2f((int)(rect.l), (int)(-rect.t + targetDimension.y - rect.h));
    }

    public Vector2f getDimension() {
        return new Vector2f(rect.w, rect.h);
    }

    public void apply(){
        glViewport((int)(rect.l), (int)(-rect.t + targetDimension.y - rect.h), (int)(rect.w), (int)(rect.h));
        //glViewport((int)(rect.l), (int)(rect.t + 500), (int)(rect.w), (int)(rect.h));
    }
}
