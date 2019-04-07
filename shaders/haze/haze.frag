#version 120

in vec4 Color;
in vec2 TexCoord;

uniform sampler2D texture;
uniform sampler2D noise;
uniform float pxPerSec;
uniform float seconds;
uniform vec2 hazeDirection;

out vec4 FragColor;

void main() {
    vec4 offsetPixel = texture2D(noise, TexCoord + hazeDirection * pxPerSec * seconds);
    vec2 offset = offsetPixel.rg;
    vec4 pixel = texture2D(texture, offset);

    FragColor = pixel * Color;
}
