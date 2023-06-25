package zelaux.arclib.utils.files;

import arc.files.Fi;

public class FileTree {
    public final Fi root;

    public FileTree(Fi fi) {
        root = fi;
        root.mkdirs();
    }

    public Fi child(String childPath) {
        Fi out = root;
        for (String s : childPath.split("/")) {
            out = out.child(s);
        }
        return out;
    }
}
