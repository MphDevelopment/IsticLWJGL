/* Very simple vertex shader that applies the model view
 * and projection matrix to the given vertex and overrides
 * the color with a constant for all vertices.
 */
#version 400


layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec4 VertexColor;
layout (location = 2) in vec2 VertexTexCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec4 Color;
out vec2 TexCoord;

void main()
{
    TexCoord = VertexTexCoord;

    Color = VertexColor;

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(VertexPosition, 1.0);
}