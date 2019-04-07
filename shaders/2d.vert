#version 120

in vec2 VertexPosition;
in vec4 VertexColor;
in vec2 VertexTexCoord;

uniform vec4 ModelViewProjectionMatrix;

out vec2 TexCoord;
out vec4 Color;

void main() {

    TexCoord = VertexTexCoord;

    Color = VertexColor;

    gl_Position = ModelViewProjectionMatrix * vec4(VertexPosition, 0, 1);
}
