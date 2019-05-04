/* Very simple fragment shader. It basically passes the
 * (interpolated) vertex color on to the individual pixels.
 */ 
#version 400

// corresponds with output from vertex shader
in vec4 Color;

out vec4 FragColor;

void main()
{
    FragColor = Color;
}