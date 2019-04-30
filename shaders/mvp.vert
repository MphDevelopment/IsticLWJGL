/* Very simple vertex shader that applies the model view
 * and projection matrix to the given vertex and overrides
 * the color with a constant for all vertices.
 */
#version 400


layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec4 VertexColor;
layout (location = 1) in vec2 VertexTexCoord;


uniform mat4 mvp;

out vec4 Color;
out vec2 TexCoord;

void main()
{
    Color  = VertexColor;
    gl_Position = mvp * vec4(VertexPosition, 1.0);
}