package System;


import org.lwjgl.opengl.GL11;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class GlStates {
    //private Mat4 modelview;
    //private Mat4 projection;
    private HashMap<Integer, Void> enabled;

    private GlStates() {
    }

    public static GlStates GetMatrix() {
        GlStates states = new GlStates();


        if (glIsEnabled(GL_TEXTURE_2D))
            states.enabled.put(GL_TEXTURE_2D, null);

        return states;
    }



}
