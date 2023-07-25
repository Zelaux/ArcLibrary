package zelaux.arclib.graphics.g3d.model.obj.mtl;

import arc.files.Fi;
import arc.struct.ObjectMap;

/** Material data container. **/
public class MTL {
    public final Fi file;

    public final ObjectMap<String, String> data = new ObjectMap<>();

    public MTL(Fi file) {
        this.file = file;
    }

    public String get(String key) {
        return data.get(key);
    }

    public void set(String key, String value) {
        data.put(key, value);
    }
}
