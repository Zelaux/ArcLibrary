attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

varying vec2 v_texCoords;

uniform mat4 u_proj;
uniform mat4 u_transf;
uniform vec3 u_transl;

void main(){
    v_texCoords = vec2(a_texCoord0.x, 1.0 - a_texCoord0.y);
    gl_Position = u_proj * u_transf * (a_position + vec4(u_transl, 0.0));
}