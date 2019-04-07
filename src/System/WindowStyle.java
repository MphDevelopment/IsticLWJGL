package System;


public enum WindowStyle {

    VISIBLE (1 << 0),
    TOPMOST (1 << 1),
    TITLEBAR (1 << 2),
    RESIZABLE (1 << 3),
    FULLSCREEN (1 << 4 + VISIBLE.bits),
    DEFAULT(VISIBLE.bits + RESIZABLE.bits + TITLEBAR.bits);

    public final int bits;
    WindowStyle(int bitwise){
        bits = bitwise;
    }

}
