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

import static org.lwjgl.opengl.ARBMultitexture.glActiveTextureARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUniform1iARB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class BumpVBO extends BufferObject {
    private Texture texture;
    private Texture bump;
    private Shader shader;

    public BumpVBO(Shader shader, BindMode mode, Vector3f center, float length, @NotNull Texture t, @NotNull Texture b) {
        super(mode);
        this.texture = t;
        this.bump = b;
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

        float normals[] = new float[]{
                0,0,-1,
                0,0,-1,
                0,0,-1,
                0,0,-1,
                0,0,-1,
                0,0,-1,

                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,

                -1,0,0,
                -1,0,0,
                -1,0,0,
                -1,0,0,
                -1,0,0,
                -1,0,0,

                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,

                0,-1,0,
                0,-1,0,
                0,-1,0,
                0,-1,0,
                0,-1,0,
                0,-1,0,

                0,1,0,
                0,1,0,
                0,1,0,
                0,1,0,
                0,1,0,
                0,1,0
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

        // convert tex coord array to buffer
        FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(texcoord.length);
        texCoordBuffer.put(texcoord);
        texCoordBuffer.flip();

        // convert tex coord array to buffer
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normals.length);
        normalBuffer.put(normals);
        normalBuffer.flip();

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

        // create vertex texcoord
        int normalBufferHandle = GL15.glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBufferHandle);
        glBufferData(GL15.GL_ARRAY_BUFFER, normalBuffer, bindMode);

        // unbind VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // create vertex array object (VAO)
        int vaoHandle = GL30.glGenVertexArrays();
        glBindVertexArray(vaoHandle);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        // assign vertex VBO to slot 0 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
        glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // assign vertex VBO to slot 1 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        // assign tex coord to slot 1 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordBufferHandle);
        glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 0, 0);

        // assign tex coord to slot 1 of VAO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBufferHandle);
        glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, 0, 0);

        // unbind VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        this.glId = vaoHandle;
    }

    @Override
    public void clear() {

    }

    @Override
    public void draw() {
        if (this.glId == 0) return ;

        glEnable(GL_TEXTURE_2D);

        GL20.glUseProgram((int)shader.getGlId());

        glBindVertexArray((int)this.glId);

        glActiveTextureARB(GL_TEXTURE0);
        texture.bind();
        glUniform1iARB(0,0);

        glActiveTextureARB(GL_TEXTURE1);
        bump.bind();
        glUniform1iARB(1,0);

        glEnableVertexAttribArray(0); // VertexPosition glsl:(location = 0)
        glEnableVertexAttribArray(1); // VertexColor glsl:(location = 1)
        glEnableVertexAttribArray(2); // VertexTexCoord glsl:(location = 2)
        glEnableVertexAttribArray(3); // VertexBumpNormal glsl:(location = 3)


        // draw VAO
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, count);
        glBindVertexArray(0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        GL20.glUseProgram(0);

        Texture.unbind();
    }
}
