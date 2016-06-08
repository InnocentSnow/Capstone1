precision mediump float;

uniform sampler2D u_texture;
uniform sampler2D u_origin;

varying vec2 v_texCoord;
varying vec2 v_originCoord;

void main()
{
    vec4 textureColor = texture2D(u_texture, v_texCoord);
    vec4 originColor = texture2D(u_origin, v_originCoord);

    if(originColor.w == 0.0) gl_FragColor = textureColor;
    else gl_FragColor = 0.8 * textureColor + 0.2 * originColor;
}
