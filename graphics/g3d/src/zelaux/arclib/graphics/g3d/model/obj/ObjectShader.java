package zelaux.arclib.graphics.g3d.model.obj;

import arc.files.Fi;
import arc.graphics.gl.Shader;

public class ObjectShader extends Shader{
    public ObjectShader(Fi vertexShader, Fi fragmentShader){
        super(vertexShader, fragmentShader);
    }

    public ObjectShader(String vertexShader, String fragmentShader){
        super(vertexShader, fragmentShader);
    }
}
