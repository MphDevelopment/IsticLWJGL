/* Very simple vertex shader that applies the model view
 * and projection matrix to the given vertex and overrides
 * the color with a constant for all vertices.
 */
#version 400


layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec3 VertexColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec3 Color;

void main()
{
    Color = VertexColor;
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(VertexPosition, 1.0);
}