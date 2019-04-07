#version 120

in vec2 TexCoord;
in vec4 Color;

uniform sampler2D texture;

out vec4 FragColor;

void main() {
    vec4 pixel = texture2D(texture, TexCoord);

    FragColor = pixel * Color;
}
