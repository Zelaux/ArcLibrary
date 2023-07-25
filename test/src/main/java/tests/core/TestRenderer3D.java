package tests.core;

import arc.*;
import arc.math.geom.*;
import zelaux.arclib.graphics.g3d.render.*;

public class TestRenderer3D extends GenericRenderer3D{
    Vec3 cameraPos = new Vec3();
    Vec3 cameraVel = new Vec3();
    Vec3 cameraRot = new Vec3(1f, 1f, 1f);
    public float zoom = 1f;

    @Override
    public void render(){
        cameraPos.add(cameraVel);

        cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        cam.position.set(cameraRot).scl(zoom).add(cameraPos);
        cam.lookAt(cameraPos);
        super.render();
    }
}
