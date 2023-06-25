attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

varying vec2 v_texCoords;

uniform mat4 u_proj;
uniform mat4 u_transf;
uniform vec3 u_transl;

void main(){
    v_texCoords = a_texCoord0;
    gl_Position = u_proj * u_transf * (a_position + vec4(u_transl, 0.0));
}