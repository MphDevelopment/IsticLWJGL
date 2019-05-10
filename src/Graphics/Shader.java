package Graphics;

import System.GlObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**http://schabby.de/opengl-shader-example/*/
///TODO When user unbind it's own shader/ IsticLWJGL must bind the default one if there is no current shader (le faire pendant window.draw(drawable))
public class Shader extends GlObject implements ConstShader {
    //TODO plus optimisé de faire comme ça ? en gros on va conserver le dernier shader (ou null) activé pour ne pas l'activer a chaque fois qu'il va être utilisé a la suite
    private static ThreadLocal<Shader> currentShader = new ThreadLocal<Shader>();
    static {
        currentShader.set(null);
    }
    public static void rebind() {
        ///TODO IMPORTANT chaque fenetre doit binder le shader mais si le shader a été bindé précédement (ThreadLocal) il ne va plus se binder
        GL20.glUseProgram(currentShader.get() != null ? (int) currentShader.get().glId : 0);
    }

    private static Shader defaultShader = null;
    public static void createDefault() {
        if (defaultShader != null) return ;

        defaultShader = new Shader();
        try {
            defaultShader.loadFromMemory(
                    "#version 130\n" +
                    "/*layout (location = 0)*/ in vec3 VertexPosition;\n" +
                    "/*layout (location = 1)*/ in vec4 VertexColor;\n" +
                    "/*layout (location = 2)*/ in vec2 VertexTexCoordinates;\n" +
                    "uniform mat4 mvp;\n" +
                    "out vec4 Color;\n" +
                    "out vec2 TexCoordinates;\n" +
                    "void main()\n" +
                    "{\n" +
                    "    Color  = VertexColor;\n" +
                    "    TexCoordinates = VertexTexCoordinates;\n" +
                    "    gl_Position = mvp * vec4(VertexPosition, 1.0);\n" +
                    "}"
                    ,


                    "#version 130\n" +
                    "in vec4 Color;\n" +
                    "in vec2 TexCoordinates;\n" +
                    "uniform sampler2D texture;\n" +
                    "out vec4 FragColor;\n" +
                    "void main()\n" +
                    "{\n" +
                    "    vec4 pixel = texture2D(texture, TexCoordinates);\n" +
                    "    FragColor = pixel * Color;\n" +
                    "}");
        } catch (IOException e) {
            throw new RuntimeException("Can't load default Shader");
        }
    }
    public static Shader getDefaultShader() {
        return defaultShader;
    }

    private HashMap<String, Integer> uniforms = new HashMap<>();

    public Shader(){}

    public Shader(String vert, String frag) throws IOException {
        loadFromFile(vert, frag);
    }

    public void loadFromFile(String vert, String frag) throws IOException {
        // create the shader program. If OK, create vertex and fragment shaders
        glId = glCreateProgram();

        // load and compile the two shaders
        int vertShader = loadAndCompileShader(vert, GL_VERTEX_SHADER);
        int fragShader = loadAndCompileShader(frag, GL_FRAGMENT_SHADER);

        glBindAttribLocation((int)glId, 0, "VertexPosition");
        glBindAttribLocation((int)glId, 1, "VertexColor");
        glBindAttribLocation((int)glId, 2, "VertexTexCoordinates");

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

    public void loadFromMemory(String vert, String frag) throws IOException {
        // create the shader program. If OK, create vertex and fragment shaders
        glId = glCreateProgram();

        // load and compile the two shaders
        int vertShader = loadAndCompileShaderFromMemory(vert, GL_VERTEX_SHADER);
        int fragShader = loadAndCompileShaderFromMemory(frag, GL_FRAGMENT_SHADER);

        glBindAttribLocation((int)glId, 0, "VertexPosition");
        glBindAttribLocation((int)glId, 1, "VertexColor");
        glBindAttribLocation((int)glId, 2, "VertexTexCoordinates");

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

    private int loadAndCompileShaderFromMemory(String code, int shaderType) {
        //vertShader will be non zero if succefully created
        int handle = glCreateShader(shaderType);

        if( handle == 0 )
        {
            throw new RuntimeException("could not created shader of type " + shaderType + " : " + glGetProgramInfoLog((int)glId, 1000));
        }

        // upload code to OpenGL and associate code with shader
        glShaderSource(handle, code);

        // compile source code into binary
        glCompileShader(handle);

        // acquire compilation status
        int shaderStatus = glGetShaderi(handle, GL20.GL_COMPILE_STATUS);

        // check whether compilation was successful
        if( shaderStatus == GL11.GL_FALSE)
        {
            throw new IllegalStateException("compilation error for shader. Reason: " + glGetShaderInfoLog(handle, 1000));
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
     * @see Shader#isBound now returns true
     */
    public void bind(){
        if (currentShader.get() != this) {
            currentShader.set(this);
            GL20.glUseProgram((int) this.glId);
        }
    }

    /**
     * Checks if 'this' is current bound shader
     * @return true if 'this' is current bound shader else false
     */
    public boolean isBound() {
        return currentShader.get() == this;
    }

    /**
     * Removes last bound shader as current shader.
     */
    public static void unbind(){
        if (currentShader.get() != null) {
            currentShader.set(null);
            GL20.glUseProgram(0);
        }
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

    /**
     * Gets the uniform specified value location inside the shader program?
     * @param name uniform value name.
     * @return uniform specified value location inside the shader program?
     */
    public int getUniformLocation(String name) {
        Integer location = uniforms.get(name);
        if (location != null) {
            return location;
        } else {
            location = glGetUniformLocation((int)this.getGlId(), name);
            uniforms.put(name, location);
            return location;
        }
    }

}
