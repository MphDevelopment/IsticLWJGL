package System;


import Graphics.Color;
import Graphics.Drawable;
import Graphics.Image;
import Graphics.Vector2i;
import com.sun.istack.internal.NotNull;

/**
 * RenderTarget is an abstract class specifying that it drawn OpenGL graphics.
 */
//TODO RenderTargets must own their own view (Camera) and viewport (Viewport).
public abstract class RenderTarget extends GlObject {
    //Multiple RenderTargets
    private static RenderTarget currentTarget = null;

    //default view parameters
    protected Camera defaultCamera;
    protected Camera camera;
    protected Viewport defaultViewport;
    protected Viewport viewport;


    protected RenderTarget(){
        super();
    }

    /**
     * Change current RenderTarget to 'this' and change view to 'this' view
     */
    protected final void setActive(){
        currentTarget = this;
        this.bind(); // on souhaite modifier ce RenderTarget seulement
        this.applyView(); // on applique sa vue et ses paramètres
    }

    /**
     * Make 'this' as the current RenderTarget.
     */
    protected abstract void bind() ;

    /**
     * Updates current RenderTarget View using Camera and Viewport
     */
    private final void applyView() {
        camera.apply();
        viewport.apply(this);
    }

    /**
     * Checks if currentTarget is 'this'.
     * @return true if currentTarget is 'this'
     */
    protected final boolean isActive(){
        return currentTarget == this;
    }



    /**
     * Change last camera to camera.
     * Setting a camera obliges the User to update camera settings when the GLFWWindow is resizing.
     * @param cam specified camera
     */
    //TODO changer de la camera pendant un affichage ne va pas remettre a jour la vue du RenderTarget
    //TODO changer les paramètres de la camera utilisée pendant un affichage ne va pas remettre a jour la vue du RenderTarget
    public final void setCamera(@NotNull Camera cam){
        this.camera = cam;
    }

    /**
     * Change last viewport to viewport.
     * Setting a viewport obliges the User to update viewport settings when the GLFWWindow is resizing.
     * @param viewport specified viewport
     */
    //TODO changer de viewport pendant un affichage ne va pas remettre a jour la vue du RenderTarget
    //TODO changer les paramètres du viewport utilisé pendant un affichage ne va pas remettre a jour la vue du RenderTarget
    public final void setViewport(@NotNull Viewport viewport){
        this.viewport = viewport;
    }


    /**
     * Clear the RenderTarget with a fill color.
     * RenderTarget is activated before calling 'clear' method.
     * @param color clear color
     */
    public abstract void clear(Color color);

    /**
     * Clear the RenderTarget with black color.
     * RenderTarget is activated before calling 'clear' method.
     */
    public abstract void clear();

    /**
     * RenderTarget is activated before calling 'draw' method of the drawable.
     * @param d drawable
     */
    public abstract void draw(Drawable d);

    /**
     * Gives the pixel dimension of the RenderTarget.
     * @return pixel dimension
     */
    public abstract Vector2i getDimension();

    /**
     * Screenshot the RenderTarget and gives an image of it.
     * @return screenshot image
     */
    public abstract Image capture() ;

}
