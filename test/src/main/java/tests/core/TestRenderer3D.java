package tests.core;

import arc.*;
import arc.math.geom.*;
import zelaux.arclib.graphics.g3d.render.*;

public class TestRenderer3D extends GenericRenderer3D{
    public Vec3 cameraTarget = new Vec3();
    public Vec3 cameraVel = new Vec3();
    public Vec3 cameraRotation = new Vec3(1f, 1f, 1f);
    public float zoom = 1f;

    @Override
    public void render(){
        cameraTarget.add(cameraVel);

        cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        cam.position.set(cameraRotation).scl(zoom).add(cameraTarget);
        cam.lookAt(cameraTarget);
        super.render();
    }
}
