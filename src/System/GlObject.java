package System;


public abstract class GlObject {
    protected long glId = 0;

    protected GlObject(){}

    protected GlObject(int glId){
        this.glId = glId;
    }

    public abstract void free();

    public long getGlId() {
        return glId;
    }
}
