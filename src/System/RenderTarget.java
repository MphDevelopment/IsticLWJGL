package System;


import Graphics.Color;
import Graphics.Drawable;

public abstract class RenderTarget extends GlContext {
    private static RenderTarget currentRenderer = null;

    protected static RenderTarget getCurrentRenderer() {
        return currentRenderer;
    }

    protected final void setCurrent() {
        currentRenderer = this;
    }

    public RenderTarget(){
        super();
    }

    public abstract void clear(Color color);

    public abstract void clear();

    public abstract void draw(Drawable d);

    @Override
    public abstract void free();
}
