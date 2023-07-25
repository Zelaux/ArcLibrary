package zelaux.arclib.utils.files;

import arc.files.Fi;
import arc.files.ZipFi;

/** Use it for get file tree in another jar file **/
public class InternalFileTree extends FileTree{
    /** @param anchor class from jar for navigate. **/
    public InternalFileTree(Class<?> anchor) {
        super(getJar(anchor));
    }

    /**
     * @param anchor class from jar
     * @return jar path
    **/
    static ZipFi getJar(Class<?> anchor) {
        String classPath = anchor.getResource("").getFile();
        classPath = classPath.substring(classPath.indexOf(":")+2);
        return new ZipFi(new Fi(classPath.substring(0, classPath.indexOf("!"))));
    }
}
