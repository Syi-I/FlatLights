#version 150

uniform sampler2D Skybox;
uniform float GameTime;

out vec4 fragColor;

void main() {
    ivec2 coord = ivec2(gl_FragCoord.xy);
    //ivec2 coord = ivec2(gl_FragCoord.xy / 2);
    vec4 skybox = texelFetch(Skybox, coord, 0);
    fragColor = skybox;

    //float wave = sin(GameTime * 0.1 + gl_FragCoord.x * 0.01 + gl_FragCoord.y * 0.01);
    //vec3 color = mix(vec3(0.2, 0.0, 0.5), vec3(0.6, 0.0, 1.0), wave * 0.5 + 0.5);
    //fragColor = vec4(color, 0.80) * skybox;

}
