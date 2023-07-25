package tests;

import arc.math.geom.Vec3;
import zelaux.arclib.graphics.g3d.model.Model;

import java.math.BigDecimal;

public class SystemObjectFactory {
    public Model model;

    public SystemObject create(Vec3 pos, Vec3 vel, long mass, long r) {
        SystemObject out = new SystemObject();

        return out;
    }

    public class SystemObject {
        // distance in

        public long
                x, y, z,
                vx, vy, vz,
                r, m;

        public void update() {

        }

        public void postUpdate() {
            x += vx;
            y += vy;
            z += vz;
        }

        public void render() {

        }
    }
}
