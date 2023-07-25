package zelaux.arclib.graphics.g3d.model.obj.mtl;

import arc.files.Fi;
import arc.struct.Seq;

import java.io.BufferedReader;
import java.io.IOException;

/** For models parsing use {@link zelaux.arclib.graphics.g3d.model.obj.ObjectModelFactory} **/
public class MTLParser {
    /** @param file file for parsing **/
    public static Seq<MTL> parse(Fi file) throws IOException {
        Seq<MTL> out = new Seq<>();
        MTL currentMTL = null;

        BufferedReader lines = new BufferedReader(file.reader());
        String line;
        while ((line = lines.readLine()) != null) {
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
