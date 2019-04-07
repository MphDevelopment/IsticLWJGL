package Graphics.VBO;


import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;

import Graphics.Drawable;
import System.GlObject;

public abstract class BufferObject extends GlObject implements Drawable {
    public enum BindMode {
        STREAM_DRAW(GL_STREAM_DRAW),
        STREAM_READ(GL_STREAM_READ),
        STREAM_COPY(GL_STREAM_COPY),
        STATIC_DRAW(GL_STATIC_DRAW),
        STATIC_READ(GL_STATIC_READ),
        STATIC_COPY(GL_STATIC_COPY),
        DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
        DYNAMIC_READ(GL_DYNAMIC_READ),
        DYNAMIC_COPY(GL_DYNAMIC_COPY);

        private int v;
        BindMode(int type){
            this.v = type;
        }

        public final int asInteger() {
            return v;
        }
    }

    protected int bindMode;
    protected int mode;
    protected int count;

    public BufferObject(BindMode mode){
        bindMode = mode.v;
    }

    public BufferObject(int mode){
        bindMode = mode;
    }

    //abstract public void bind();

    /**
     * Clear method release memory used into the RAM
     */
    abstract public void clear() ;

    abstract public void draw() ;

    @Override
    public void free(){
        glBindBuffer((int)glId, 0);
        glDeleteBuffers((int)glId);
        glId = 0;
    }
}
