package zelaux.arclib.graphics.g3d.render;

import arc.math.geom.Mat3D;

public interface Renderer3D {
    Mat3D getProjMat();

    void init();
    boolean shouldRender();
    void render();
}
