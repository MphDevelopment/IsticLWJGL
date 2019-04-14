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
    private FloatRect rect;

    /**
     * Create a viewport.
     * @param rect the rectangle where graphics will be drawn.
     */
    public Viewport(FloatRect rect){
        this.rect = rect;
    }

    /**
     * Top-left corner depends on which RenderTarget we drawn.
     * @param target
     * @return the top-left corner of the viewport.
     */
    public Vector2f getTopleftCorner(RenderTarget target){
        return new Vector2f((int)(rect.l), (int)(-rect.t + target.getDimension().y - rect.h));
    }

    /**
     *
     * @return
     */
    public Vector2f getDimension() {
        return new Vector2f(rect.w, rect.h);
    }

    public void setDimension(Vector2f dimension) {
        rect.w = dimension.x;
        rect.h = dimension.y;
    }

    public void setTopleftCorner(Vector2f position) {
        rect.l = position.x;
        rect.t = position.y;
    }

    public void apply(RenderTarget target){
        glViewport((int)(rect.l), (int)(-rect.t + target.getDimension().y - rect.h), (int)(rect.w), (int)(rect.h));
    }
}
