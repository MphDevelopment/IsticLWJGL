/* Very simple fragment shader. It basically passes the
 * (interpolated) vertex color on to the individual pixels.
 */
#version 400

// corresponds with output from vertex shader
in vec4 Color;
in vec2 TexCoord;

uniform sampler2D tex0;

out vec4 FragColor;

void main()
{
    vec4 pixel = texture(tex0, TexCoord);
    // assign vertex color to pixel color
    FragColor = Color * pixel;
}