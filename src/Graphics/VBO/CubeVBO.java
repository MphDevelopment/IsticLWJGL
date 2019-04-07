package Graphics.VBO;


import Graphics.Shader;
import Graphics.Texture;
import Graphics.Vector3f;
import com.sun.istack.internal.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class CubeVBO extends BufferObject {
    private Shader shader;
   // private Texture texture = null;

    public CubeVBO(Shader shader, BindMode mode, Vector3f center, float length) {
        super(mode);
        this.count = 36;
        this.shader = shader;

        length /= 2.f;

        float positionData[] = new float[]{
                // back face
                center.x - length, center.y - length, center.z - length,
                center.x + length, center.y - length, center.z - length,
                center.x - length, center.y + length, center.z - length,
                center.x + length, center.y - length, center.z - length,
                center.x - length, center.y + length, center.z - length,
                center.x + length, center.y + length, center.z - length,

                //front face
                center.x - length, center.y - length, center.z + length,
                center.x + length, center.y - length, center.z + length,
                center.x - length, center.y + length, center.z + length,
                center.x + length, center.y - length, center.z + length,
                center.x - length, center.y + length, center.z + length,
                center.x + length, center.y + length, center.z + length,

                //left face
                center.x - length, center.y - length, center.z - length,
                center.x - length, center.y - length, center.z + length,
                center.x - length, center.y + length, center.z - length,
                center.x - length, center.y - length, center.z + length,
                center.x - length, center.y + length, center.z - length,
                center.x - length, center.y + length, center.z + length,

                //right face
                center.x + length, center.y - length, center.z - length,
                center.x + length, center.y - length, center.z + length,
                center.x + length, center.y + length, center.z - length,
                center.x + length, center.y - length, center.z + length,
                center.x + length, center.y + length, center.z - length,
                center.x + length, center.y + length, center.z + length,

                //down face
                center.x - length, center.y - length, center.z - length,
                center.x + length, center.y - length, center.z - length,
                center.x - length, center.y - length, center.z + length,
                center.x + length, center.y - length, center.z - length,
                center.x - length, center.y - length, center.z + length,
                center.x + length, center.y - length, center.z + length,

                //top face
                center.x - length, center.y + length, center.z - length,
                center.x + length, center.y + length, center.z - length,
                center.x - length, center.y + length, center.z + length,
                center.x + length, center.y + length, center.z - length,
                center.x - length, center.y + length, center.z + length,
                center.x + length, center.y + length, center.z + length
        };

        float colorData[] = new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,

                1,0,1,
                1,0,1,
                1,0,1,
                1,0,1,
                1,0,1,
                1,0,1,

                1,1,0,
                1,1,0,
                1,1,0,
                1,1,0,
                1,1,0,
                1,1,0,

                0,1,1,
                0,1,1,
                0,1,1,
                0,1,1,
                0,1,1,
                0,1,1,

                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,

                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
        };

        // convert vertex array to buffer
        FloatBuffer positionBuffer = BufferUtils.createFloatBuffer(positionData.length);
        positionBuffer.put(positionData);
        positionBuffer.flip();

        // convert color array to buffer
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorData.length);
        colorBuffer.put(colorData);
        colorBuffer.flip();

        // create vertex byffer object (VBO) for vertices
        int positionBufferHandle = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
        glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, bindMode);

        // create VBO for color values
        int colorBufferHandle = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, bindMode);

        // unbind VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // create vertex array object (VAO)
        int vaoHandle = GL30.glGenVertexArrays();
        glBindVertexArray(vaoHandle);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // assign vertex VBO to slot 0 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
        glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // assign vertex VBO to slot 1 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        // unbind VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        this.glId = vaoHandle;
    }

    /**
    public CubeVBO(Shader shader, BindMode mode, Vector3f center, float length, @NotNull Texture t) {
        super(mode);
        this.texture = texture;
        this.count = 36;
        this.shader = shader;

        length /= 2.f;

        float positionData[] = new float[]{
                // back face
                center.x - length, center.y - length, center.z - length,
                center.x + length, center.y - length, center.z - length,
                center.x - length, center.y + length, center.z - length,
                center.x + length, center.y - length, center.z - length,
                center.x - length, center.y + length, center.z - length,
                center.x + length, center.y + length, center.z - length,

                //front face
                center.x - length, center.y - length, center.z + length,
                center.x + length, center.y - length, center.z + length,
                center.x - length, center.y + length, center.z + length,
                center.x + length, center.y - length, center.z + length,
                center.x - length, center.y + length, center.z + length,
                center.x + length, center.y + length, center.z + length,

                //left face
                center.x - length, center.y - length, center.z - length,
                center.x - length, center.y - length, center.z + length,
                center.x - length, center.y + length, center.z - length,
                center.x - length, center.y - length, center.z + length,
                center.x - length, center.y + length, center.z - length,
                center.x - length, center.y + length, center.z + length,

                //right face
                center.x + length, center.y - length, center.z - length,
                center.x + length, center.y - length, center.z + length,
                center.x + length, center.y + length, center.z - length,
                center.x + length, center.y - length, center.z + length,
                center.x + length, center.y + length, center.z - length,
                center.x + length, center.y + length, center.z + length,

                //down face
                center.x - length, center.y - length, center.z - length,
                center.x + length, center.y - length, center.z - length,
                center.x - length, center.y - length, center.z + length,
                center.x + length, center.y - length, center.z - length,
                center.x - length, center.y - length, center.z + length,
                center.x + length, center.y - length, center.z + length,

                //top face
                center.x - length, center.y + length, center.z - length,
                center.x + length, center.y + length, center.z - length,
                center.x - length, center.y + length, center.z + length,
                center.x + length, center.y + length, center.z - length,
                center.x - length, center.y + length, center.z + length,
                center.x + length, center.y + length, center.z + length
        };

        float colorData[] = new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,

                1,0,1,
                1,0,1,
                1,0,1,
                1,0,1,
                1,0,1,
                1,0,1,

                1,1,0,
                1,1,0,
                1,1,0,
                1,1,0,
                1,1,0,
                1,1,0,

                0,1,1,
                0,1,1,
                0,1,1,
                0,1,1,
                0,1,1,
                0,1,1,

                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,

                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
        };

        float texcoord[] = new float[]{
                0,0,
                1,0,
                0,1,
                1,0,
                0,1,
                1,1,

                0,0,
                1,0,
                0,1,
                1,0,
                0,1,
                1,1,

                0,0,
                1,0,
                0,1,
                1,0,
                0,1,
                1,1,

                0,0,
                1,0,
                0,1,
                1,0,
                0,1,
                1,1,

                0,0,
                1,0,
                0,1,
                1,0,
                0,1,
                1,1,

                0,0,
                1,0,
                0,1,
                1,0,
                0,1,
                1,1,
        };

        // convert vertex array to buffer
        FloatBuffer positionBuffer = BufferUtils.createFloatBuffer(positionData.length);
        positionBuffer.put(positionData);
        positionBuffer.flip();

        // convert color array to buffer
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorData.length);
        colorBuffer.put(colorData);
        colorBuffer.flip();

        // convert color array to buffer
        FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(colorData.length);
        colorBuffer.put(texcoord);
        colorBuffer.flip();

        // create vertex byffer object (VBO) for vertices
        int positionBufferHandle = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
        glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, bindMode);

        // create VBO for color values
        int colorBufferHandle = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, bindMode);

        // create vertex texcoord
        int texcoordBufferHandle = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordBufferHandle);
        glBufferData(GL15.GL_ARRAY_BUFFER, texCoordBuffer, bindMode);

        // unbind VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // create vertex array object (VAO)
        int vaoHandle = GL30.glGenVertexArrays();
        glBindVertexArray(vaoHandle);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // assign vertex VBO to slot 0 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
        glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // assign vertex VBO to slot 1 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        if (this.texture != null) {
            glEnableVertexAttribArray(2);
            // assign tex coord to slot 1 of VAO
            glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordBufferHandle);
            glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 0, 0);
        }

        // unbind VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        this.glId = vaoHandle;
    }
    */
    public CubeVBO(Shader shader, Vector3f center) {
        super(BindMode.STATIC_DRAW.asInteger());
        this.count = 3;
        this.shader = shader;

        // create vertex data
        float[] positionData = new float[]{
                10.f + center.x, -1.f + center.y, 0.f + center.z,
                -10.f + center.x, 10.f + center.y, 10.f + center.z,
                -1.f + center.x, -10.f + center.y, 0.f + center.z
        };

        // create color data
        float[] colorData = new float[]{
                0f, 0f, 1f,
                1f, 1f, 0f,
                0f, 1f, 0f
        };

        // convert vertex array to buffer
        FloatBuffer positionBuffer = BufferUtils.createFloatBuffer(positionData.length);
        positionBuffer.put(positionData);
        positionBuffer.flip();

        // convert color array to buffer
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorData.length);
        colorBuffer.put(colorData);
        colorBuffer.flip();

        // create vertex byffer object (VBO) for vertices
        int positionBufferHandle = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
        glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);

        // create VBO for color values
        int colorBufferHandle = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);

        // unbind VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // create vertex array object (VAO)
        int vaoHandle = GL30.glGenVertexArrays();
        glBindVertexArray(vaoHandle);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // assign vertex VBO to slot 0 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
        glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // assign vertex VBO to slot 1 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        // unbind VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        this.glId = vaoHandle;
    }


    @Override
    public void clear() {
        // do nothing
    }

    @Override
    public void draw() {
        if (this.glId == 0) return ;

        GL20.glUseProgram((int)shader.getGlId());

        glEnable(GL_TEXTURE_2D);
        /*if (this.texture != null) {
            glActiveTexture(GL_TEXTURE0);

            // bind texture to shader
            this.texture.bind();
            glUniform1i(glGetUniformLocation(shader.getGlId(), "texture"), 0);
        }*/
        // bind vertex and color data
        glBindVertexArray((int)this.glId);
        //glEnableVertexAttribArray(0); // VertexPosition glsl:(location = 0)
        //glEnableVertexAttribArray(1); // VertexColor glsl:(location = 1)
        /*if (this.texture != null) {
            //glEnableVertexAttribArray(2); // VertexTexCoord glsl:(location = 2)
        }*/

        // draw VAO
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, count);
        glBindVertexArray(0);


        /*if (this.texture != null) {
            Texture.unbind();
        }*/

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        GL20.glUseProgram(0);
    }

}
