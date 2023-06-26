package zelaux.arclib.graphics.g3d.model.obj.obj;

import arc.files.Fi;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.FloatSeq;
import arc.struct.IntSeq;
import arc.struct.Seq;

// FIXME a lot of garbage
/** For models parsing use {@link zelaux.arclib.graphics.g3d.model.obj.ObjectModelFactory} **/
public class OBJParser {
    /** File for parse. **/
    public Fi file;

    public OBJParser(Fi file) {
        this.file = file;
    }

    /** Parse raw objects from {@link OBJParser#file}. **/
    public Seq<OBJ> parse() {
        Seq<OBJ> out = new Seq<>();
        OBJ currentObj = null;
        Fi mtlLib = null;

        Seq<Vec3> vs = new Seq<>(), vns = new Seq<>();
        Seq<Vec2> vts = new Seq<>();

        int faces = 0;
        IntSeq facesIndexes = new IntSeq();
        FloatSeq vertices = new FloatSeq();

        String data = file.readString();

        Seq<String> lines = new Seq<>(data.split("\n"));
        for (String line: lines) {
            Seq<String> args = new Seq<>(line.split(" "));
            String key = args.first();
            args.remove(0);

            if (key.equals("mtllib")) {
                mtlLib = file.parent().child(args.first());
            } else if (key.equals("o")) {
                if (currentObj != null) {
                    out.add(currentObj);
                    constructObj(currentObj, facesIndexes, vs, vts, vns, vertices);
                    // reset all sequences
                    facesIndexes.clear();
                    vs.clear();
                    vts.clear();
                    vns.clear();
                    vertices.clear();
                }
                currentObj = new OBJ(file);
                currentObj.name = args.first();
                currentObj.mtlFile = mtlLib;
            } else if (key.equals("v")) {
                float[] vert = toFloats(args.toArray()).toArray();
                vs.add(new Vec3(vert));
            } else if (key.equals("vt")) {
                float[] vert = toFloats(args.toArray()).toArray();
                vts.add(new Vec2(vert[0], vert[1]));
            } else if (key.equals("vn")) {
                float[] vert = toFloats(args.toArray()).toArray();
                vns.add(new Vec3(vert));
            } else if (key.equals("usemtl")) {
                currentObj.mtlName = args.first();
            } else if (key.equals("f")) {
                faces++;
                args.each(str -> {
                    facesIndexes.addAll(parseIntegers(str, "/"));
                });
            }
        }

        // yes code copying (-_-)
        if (currentObj != null) {
            out.add(currentObj);
            constructObj(currentObj, facesIndexes, vs, vts, vns, vertices);
            // reset all sequences
            facesIndexes.clear();
            vs.clear();
            vts.clear();
            vns.clear();
            vertices.clear();
        }

        return out;
    }

    /** Constructs object's vertices. **/
    void constructObj(OBJ obj, IntSeq facesIndexes, Seq<Vec3> vs,
                             Seq<Vec2> vts, Seq<Vec3> vns, FloatSeq vertices) {
        for (int i = 0; i < facesIndexes.size; i += 3) {
            Vec3 v = vs.get(facesIndexes.get(i)-1);
            Vec2 vt = vts.get(facesIndexes.get(i+1)-1);
            Vec3 vn = vns.get(facesIndexes.get(i+2)-1);

            vertices.addAll(
                    v.x, v.y, v.z,
                    vt.x, vt.y,
                    vn.x, vn.y, vn.z
            );
        }
        obj.vertices = vertices.toArray();
    }

    /** Parse floats from list of strings. **/
    public static FloatSeq toFloats(String... strings) {
        FloatSeq out = new FloatSeq();
        for (String string : strings) {
            out.add(Float.parseFloat(string));
        }
        return out;
    }

    /** Parse floats from string. **/
    public static FloatSeq parseFloats(String v, String sep) {
        return toFloats(v.split(sep));
    }

    /** Parse integers from list of strings. **/
    public static IntSeq toIntegers(String... strings) {
        IntSeq out = new IntSeq();
        for (String string : strings) {
            out.add(Integer.parseInt(string));
        }
        return out;
    }

    /** Parse integers from string. **/
    public static IntSeq parseIntegers(String v, String sep) {
        return toIntegers(v.split(sep));
    }
}
