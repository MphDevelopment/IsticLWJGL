/* Very simple vertex shader that applies the model view
 * and projection matrix to the given vertex and overrides
 * the color with a constant for all vertices.
 */
#version 400


layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec3 VertexColor;
layout (location = 2) in vec2 VertexTexCoord;
layout (location = 3) in vec3 VertexBumpNormal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec3 Color;
out vec2 TexCoord;
out vec3 Normal;

void main()
{
    Color = VertexColor;

    TexCoord = VertexTexCoord;

    Normal = VertexBumpNormal;

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(VertexPosition, 1.0);
}