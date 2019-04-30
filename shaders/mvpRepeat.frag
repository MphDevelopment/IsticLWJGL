/** Texture sub-rectangle repeat
*/
#version 400

// corresponds with output from vertex shader
in vec4 Color;
in vec2 TexCoord;

uniform bool repeat = true;
uniform sampler2D texture;
uniform vec2 tl;
uniform vec2 wh;

out vec4 FragColor;

double modf(double v, double modulus) {
    while (v < 0 || v > modulus) {
        if (v < 0) v += modulus;
        else v -= modulus;
    }

    return v;
}

void main()
{
    if (repeat) {
        TexCoord = vec2 (
            modf(TexCoord.x - tl.x, wh.x) + tl.x,
            modf(TexCoord.y - tl.x, wh.y) + tl.y
        );
    }

    vec4 pixel = texture2D(texture, TexCoord);
	// assign vertex color to pixel color
    FragColor = pixel * Color;
}