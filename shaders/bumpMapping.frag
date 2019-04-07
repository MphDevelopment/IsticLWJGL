/* Very simple fragment shader. It basically passes the
 * (interpolated) vertex color on to the individual pixels.
 */
#version 400

// corresponds with output from vertex shader
in vec3 Color;
in vec2 TexCoord;
in vec3 Normal;

uniform sampler2D tex0;
uniform sampler2D noise;
//uniform float NoiseFactor = 1.f;

out vec4 FragColor;

void main()
{
    vec3 normal = normalize(texture2D(noise, TexCoord).rgb * 2.0 - 1.0);

    //offset = normal;
    float diffuse = dot(Normal, normal);

    vec4 pixel = texture(tex0, TexCoord);
    // assign vertex color to pixel color
    FragColor = vec4(Color, 1.0) * pixel * diffuse;
}