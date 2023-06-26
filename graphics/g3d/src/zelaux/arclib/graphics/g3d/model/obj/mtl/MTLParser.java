package zelaux.arclib.graphics.g3d.model.obj.mtl;

import arc.files.Fi;
import arc.struct.Seq;

/** For models parsing use {@link zelaux.arclib.graphics.g3d.model.obj.ObjectModelFactory} **/
public class MTLParser {
    /** File for parse. **/
    public Fi file;

    public MTLParser(Fi file) {
        this.file = file;
    }

    /** Parse raw materials from {@link MTLParser#file} **/
    public Seq<MTL> parse() {
        Seq<MTL> out = new Seq<>();
        MTL currentMTL = null;

        String string = file.readString();
        string = string.replaceAll("\r", "");
        Seq<String> lines = new Seq<>(string.split("\n"));

        for (String line : lines) {
            if (line.split(" ").length < 2)
                continue;
            String k = line.split(" ")[0];
            String v = line.split(" ")[1];
            if (k.equals("#") || k.equals(""))
                continue;
            if (k.equals("newmtl")) {
                currentMTL = new MTL(file);
                currentMTL.set("name", v);
                out.add(currentMTL);
            } else {
                currentMTL.set(k, v);
            }
        }

        return out;
    }
}
