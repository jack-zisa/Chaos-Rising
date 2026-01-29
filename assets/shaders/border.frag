#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

uniform vec2 u_pixelSize;
uniform vec4 u_borderColor;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
    vec4 center = texture2D(u_texture, v_texCoords);

    // If the pixel itself is visible, draw it normally
    if (center.a > 0.0) {
        gl_FragColor = center * v_color;
        return;
    }

    // Sample neighbors
    float alpha = 0.0;
    alpha += texture2D(u_texture, v_texCoords + vec2(u_pixelSize.x, 0.0)).a;
    alpha += texture2D(u_texture, v_texCoords + vec2(-u_pixelSize.x, 0.0)).a;
    alpha += texture2D(u_texture, v_texCoords + vec2(0.0, u_pixelSize.y)).a;
    alpha += texture2D(u_texture, v_texCoords + vec2(0.0, -u_pixelSize.y)).a;

    // Sample corners
    alpha += texture2D(u_texture, v_texCoords + vec2(u_pixelSize.x, u_pixelSize.y)).a;
    alpha += texture2D(u_texture, v_texCoords + vec2(-u_pixelSize.x, u_pixelSize.y)).a;
    alpha += texture2D(u_texture, v_texCoords + vec2(u_pixelSize.x, -u_pixelSize.y)).a;
    alpha += texture2D(u_texture, v_texCoords + vec2(-u_pixelSize.x, -u_pixelSize.y)).a;

    // If any neighbor is solid, draw outline
    if (alpha > 0.0) {
        gl_FragColor = u_borderColor;
    } else {
        discard;
    }
}
