package zelaux.arclib.utils.files;

import arc.files.Fi;
import arc.files.ZipFi;

/** {@link Fi} don't works correctly with internal files. **/
public class InternalFileTree extends FileTree{
    public InternalFileTree(Class<?> anchor) {
        super(getJar(anchor));
    }

    // java issue
    public static ZipFi getJar(Class<?> anchor) {
        String classPath = anchor.getResource("").getFile();
        classPath = classPath.substring(classPath.indexOf(":")+2);
        return new ZipFi(new Fi(classPath.substring(0, classPath.indexOf("!"))));
    }
}
