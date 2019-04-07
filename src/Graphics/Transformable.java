package Graphics;


public abstract class Transformable {
    protected float x=0, y=0;
    protected float ox=0, oy=0;
    protected float sx=1,sy=1;

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }
    public void setOrigin(float x, float y){
        this.ox = x;
        this.oy = y;
    }
    public void setScale(float x, float y){
        this.sx = x;
        this.sy = y;
    }

    public float getX() { return x;}
    public float getY() { return y;}
    public float getOriginX() { return ox;}
    public float getOriginY() { return oy;}
    public float getScaleX() { return sx;}
    public float getScaleY() { return sy;}

    public Vector2f getPosition(){
        return new Vector2f(x, y);
    }
    public Vector2f getOrigin(){
        return new Vector2f(ox, oy);
    }
    public Vector2f getScale(){
        return new Vector2f(sx, sy);
    }


}
