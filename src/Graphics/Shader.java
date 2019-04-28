package Graphics;

import System.GlObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**http://schabby.de/opengl-shader-example/*/
public class Shader extends GlObject {
    //private ThreadLocal<Shader> current = new ThreadLocal<Shader>();

    public Shader(String vert, String frag) throws IOException {
        // create the shader program. If OK, create vertex and fragment shaders
        glId = glCreateProgram();

        // load and compile the two shaders
        int vertShader = loadAndCompileShader(vert, GL_VERTEX_SHADER);
        int fragShader = loadAndCompileShader(frag, GL_FRAGMENT_SHADER);

        // attach the compiled shaders to the program
        glAttachShader((int)glId, vertShader);
        glAttachShader((int)glId, fragShader);

        // now link the program
        glLinkProgram((int)glId);

        // validate linking
        if (glGetProgrami((int)glId, GL_LINK_STATUS) == GL11.GL_FALSE)
        {
            throw new RuntimeException("could not link shader. Reason: " + glGetProgramInfoLog((int)glId, 1000));
        }

        // perform general validation that the program is usable
        glValidateProgram((int)glId);

        if (glGetProgrami((int)glId, GL_VALIDATE_STATUS) == GL11.GL_FALSE)
        {
            throw new RuntimeException("could not validate shader. Reason: " + glGetProgramInfoLog((int)glId, 1000));
        }
    }

    private int loadAndCompileShader(String filename, int shaderType) {
        //vertShader will be non zero if succefully created
        int handle = glCreateShader(shaderType);

        if( handle == 0 )
        {
            throw new RuntimeException("could not created shader of type "+shaderType+" for file "+filename+". "+ glGetProgramInfoLog((int)glId, 1000));
        }

        // load code from file into String
        String code = loadFile(filename);

        // upload code to OpenGL and associate code with shader
        glShaderSource(handle, code);

        // compile source code into binary
        glCompileShader(handle);

        // acquire compilation status
        int shaderStatus = glGetShaderi(handle, GL20.GL_COMPILE_STATUS);

        // check whether compilation was successful
        if( shaderStatus == GL11.GL_FALSE)
        {
            throw new IllegalStateException("compilation error for shader ["+filename+"]. Reason: " + glGetShaderInfoLog(handle, 1000));
        }

        return handle;
    }

    /**
     * Loads a text file and return it as a String.
     */
    private String loadFile(String filename) {
        StringBuilder vertexCode = new StringBuilder();
        String line;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while( (line = reader.readLine()) != null )
            {
                vertexCode.append(line);
                vertexCode.append('\n');
            }
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("unable to load shader from file ["+filename+"]", e);
        }

        return vertexCode.toString();
    }

    /**
     * Sets 'this' as the current shader program
     */
    public void bind(){
        GL20.glUseProgram((int)this.glId);
    }

    /**
     *
     */
    public static void unbind(){
        GL20.glUseProgram(0);
    }

    /**
     * Frees the shader from the GPU.
     */
    @Override
    public void free() {
        glLinkProgram((int)glId);
        glDeleteShader((int)glId);
        glUseProgram(0);
        glId = 0;
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation((int)this.getGlId(), name);
    }

}
