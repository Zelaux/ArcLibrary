package zelaux.arclib.graphics.g3d.model.obj;

import arc.files.Fi;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.struct.Seq;
import arc.util.ArcRuntimeException;
import arc.util.Log;
import zelaux.arclib.graphics.g3d.model.obj.mtl.MTL;
import zelaux.arclib.graphics.g3d.model.obj.mtl.MTLParser;
import zelaux.arclib.graphics.g3d.model.obj.obj.OBJ;
import zelaux.arclib.graphics.g3d.model.obj.obj.OBJParser;

import java.io.IOException;

/** Use it for models parsing. **/
public class ObjectModelFactory {
    /** Loads models from given object file.
     * @param objFile .obj file
     * @param shader model's shaders
     **/
    public static Seq<OBJModel> create(Fi objFile, ObjectShader shader) throws IOException {
        Seq<OBJModel> out = new Seq<>();

        Seq<OBJ> objs = OBJParser.parse(objFile);
        Seq<MTL> mtls = objs.flatMap(o -> {
            try {
                return MTLParser.parse(o.mtlFile);
            } catch (IOException e) {
                throw new ArcRuntimeException(e);
            }
        });

        objs.each(obj -> {
            MTL objMtl = mtls.find(mtl -> mtl.file.equals(obj.mtlFile) && mtl.get("name").equals(obj.mtlName));
            Texture objMtlTexture = new Texture(objMtl.file.parent().child(objMtl.get("map_Kd")));
            out.add(new OBJModel(obj, objMtl, objMtlTexture, shader));
        });

        return out;
    }
}
