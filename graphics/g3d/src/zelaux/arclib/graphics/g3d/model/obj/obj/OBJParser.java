package zelaux.arclib.graphics.g3d.model.obj.obj;

import arc.files.Fi;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.FloatSeq;
import arc.struct.IntSeq;
import arc.struct.Seq;

import java.io.*;

// FIXME a lot of garbage

/** For models parsing use {@link zelaux.arclib.graphics.g3d.model.obj.ObjectModelFactory} **/
public class OBJParser{
    static Seq<Vec3> vs = new Seq<>(), vns = new Seq<>();
    static Seq<Vec2> vts = new Seq<>();
    static IntSeq facesIndexes = new IntSeq();
    static FloatSeq vertices = new FloatSeq();

    /** @param file file for parsing **/
    public static Seq<OBJ> parse(Fi file){
        Seq<OBJ> out = new Seq<>();
        OBJ currentObj = null;
        Fi mtlLib = null;


        int faces = 0;


        try(BufferedReader lines = file.reader(1 << 13)){
            String line;
            while((line = lines.readLine()) != null){
                String[] strings = line.split(" ");
                String key = strings[0];
                String[] args = new String[strings.length - 1];
                System.arraycopy(strings, 1, args, 0, args.length);

                switch(key){
                    case "mtllib":
                        mtlLib = file.parent().child(args[0]);
                        break;
                    case "o":
                        finishObject(out, currentObj);
                        currentObj = new OBJ(file);
                        currentObj.name = args[0];
                        currentObj.mtlFile = mtlLib;
                        break;
                    case "v":{
                        float[] vert = getFloats(args);
                        vs.add(new Vec3(vert));
                        break;
                    }
                    case "vt":{
                        float[] vert = getFloats(args);
                        vts.add(new Vec2(vert[0], vert[1]));
                        break;
                    }
                    case "vn":{
                        float[] vert = getFloats(args);
                        vns.add(new Vec3(vert));
                        break;
                    }
                    case "usemtl":
                        currentObj.mtlName = args[0];
                        break;
                    case "f":
                        faces++;
                        for(String str : args){
                            parseIntegers(facesIndexes, str, "/");
                        }
                        break;
                }
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        finishObject(out, currentObj);
        return out;
    }

    private static void finishObject(Seq<OBJ> out, OBJ currentObj){
        if(currentObj != null){
            out.add(currentObj);
            constructObj(currentObj);
            // reset all sequences
            reset();
        }
    }

    private static void reset(){
        facesIndexes.clear();
        vs.clear();
        vts.clear();
        vns.clear();
        vertices.clear();
    }

    /** Parse floats from list of strings. **/
    private static float[] getFloats(String[] args){
        float[] floats = new float[args.length];
        for(int i = 0; i < floats.length; i++){
            floats[i] = Float.parseFloat(args[i]);
        }
        return floats;
    }

    /** Constructs object's vertices. **/
    static void constructObj(OBJ obj){
        vertices.clear();
        for(int i = 0; i < facesIndexes.size; i += 3){
            Vec3 v = vs.get(facesIndexes.get(i) - 1);
            Vec2 vt = vts.get(facesIndexes.get(i + 1) - 1);
            Vec3 vn = vns.get(facesIndexes.get(i + 2) - 1);

            vertices.addAll(
            v.x, v.y, v.z,
            vt.x, vt.y,
            vn.x, vn.y, vn.z
            );
        }
        obj.vertices = vertices.toArray();
    }


    /** Parse integers from list of strings. **/
    public static IntSeq toIntegers(IntSeq out, String... strings){
        for(String string : strings){
            out.add(Integer.parseInt(string));
        }
        return out;
    }

    /** Parse integers from string. **/
    public static IntSeq parseIntegers(IntSeq out, String v, String sep){
        return toIntegers(out, v.split(sep));
    }
}
