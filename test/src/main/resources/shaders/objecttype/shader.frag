uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main(){
    vec4 c = texture2D(u_texture, v_texCoords);
    gl_FragColor = c;
}