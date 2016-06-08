uniform float u_dx;
attribute vec2	a_position;
attribute vec2  a_texCoord;

varying vec2 v_texCoord;
varying vec2 v_originCoord;

void main()
{
    gl_Position = vec4(a_position.x + u_dx, a_position.y, 0.0, 1.0);
    v_texCoord = a_texCoord;
    v_originCoord = 0.5 * gl_Position.xy + vec2(0.5, 0.5);
}
