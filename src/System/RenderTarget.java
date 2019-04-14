package System;


import Graphics.Color;
import Graphics.Drawable;
import Graphics.Image;
import Graphics.Vector2i;

//TODO RenderTargets must own their own view (Camera) and viewport (Viewport).
public abstract class RenderTarget extends GlObject {
    private static RenderTarget currentRenderer = null;

    protected static RenderTarget getCurrentRenderer() {
        return currentRenderer;
    }

    protected final void setCurrent() {
        currentRenderer = this;
    }

    //protected Camera camera;
    //protected Viewport viewport;

    public RenderTarget(){
        super();
    }

    //TODO on doit faire en sorte que les matrices se mettent a jour entre chaque different bind de RenderTarget
    public abstract void bind();

    public abstract void clear(Color color);

    public abstract void clear();

    public abstract void draw(Drawable d);

    public abstract Vector2i getDimension();

    public abstract Image capture() ;

}
