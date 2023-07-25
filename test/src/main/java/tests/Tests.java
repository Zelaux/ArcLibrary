package tests;

import arc.ApplicationCore;
import arc.Core;
import arc.Files;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.files.Fi;
import arc.graphics.Gl;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g3d.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import tests.core.*;
import zelaux.arclib.graphics.g3d.model.obj.ObjectModelFactory;
import zelaux.arclib.graphics.g3d.model.obj.ObjectShader;

import java.io.IOException;

public class Tests extends ApplicationCore{
    public static void main(String[] args){
        /*Log.info(new BigNumber(4).set(0b11).right());
        Log.info(new BigNumber(4).set(0b10).mul(new BigNumber(4).set(0b11)));*/

        new SdlApplication(new Tests(), new SdlConfig(){{
            gl30 = true;
            title = "G3D test";
            maximized = true;
        }});
    }

    boolean upd = false;

    @Override
    public void setup(){
        Time.mark();

        TestVars.renderer3D = new TestRenderer3D();
        TestVars.renderer3D.init();
        TestVars.renderer3D.models.addAll(ObjectModelFactory.create(new Fi("models/cube/models.obj", Files.FileType.internal),
//            TestVars.renderer3D.models.addAll(ObjectModelFactory.create(new Fi("models/polar/polar.obj", Files.FileType.internal),
        new ObjectShader(new Fi("shaders/objecttype/shader.vert", Files.FileType.internal),
        new Fi("shaders/objecttype/shader.frag", Files.FileType.internal))));

        Core.batch = new SortedSpriteBatch();

        Log.info("[Tests] Setup time: @ms", Time.elapsed());


        Core.input.addProcessor(new InputProcessor(){
            @Override
            public boolean keyDown(KeyCode keycode){
                return InputProcessor.super.keyDown(keycode);
            }

            @Override
            public boolean keyUp(KeyCode keycode){
                return InputProcessor.super.keyUp(keycode);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer){
                upd = true;
                return InputProcessor.super.touchDragged(screenX, screenY, pointer);
            }

            @Override
            public boolean scrolled(float amountX, float amountY){
                TestVars.renderer3D.zoom = Mathf.clamp(TestVars.renderer3D.zoom + amountY * 0.1f * Mathf.clamp(TestVars.renderer3D.zoom, 0.1f, 10000f), 0.1f, 10000f);
                return false;
            }
        });
//        prevMouse.set(Core.input.mouse());
    }

    float deltaAngle = 0;
    Vec3 prevMouseRad = new Vec3();
    Vec3 curMouseRad = new Vec3();

    Vec3 normal(Vec3 a, Vec3 b, Vec3 c){
        float Ax = b.x - a.x;
        float Ay = b.y - a.y;
        float Az = b.z - a.z;

        float Bx = -a.x;
        float By = -a.y;
        float Bz = -a.z;
        float Nx = Ay * Bz - Az * By;
        float Ny = Az * Bx - Ax * Bz;
        float Nz = Ax * By - Ay * Bx;
        c.set(Nx, Ny, Nz).nor();
        return c;
    }

    @Override
    public void update(){
        Camera3D cam = TestVars.renderer3D.cam;
        cam.unproject(curMouseRad.set(Core.input.mouse(), 0));
        curMouseRad.nor();


        cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        cam.position.set(1, 0, 0).scl(TestVars.renderer3D.zoom).add(TestVars.renderer3D.cameraTarget);
        cam.lookAt(TestVars.renderer3D.cameraTarget);
        cam.update();


//        cam.unproject(curMouseRad.set(Core.graphics.getWidth()-Core.input.mouseX(),Core.graphics.getHeight()-Core.input.mouseY(), 0));
        cam.unproject(Tmp.v32.set(Core.input.mouse(), 0));
        Tmp.v32.nor();
        cam.unproject(Tmp.v31.set(Core.input.mouseX() - Core.input.deltaX(), Core.input.mouseY() - Core.input.deltaY(), 0));
        Tmp.v31.nor();
        deltaAngle = Tmp.v32.angle(Tmp.v31);

        if(Core.input.keyDown(KeyCode.mouseLeft) && upd && !curMouseRad.equals(prevMouseRad)){
            float angle = deltaAngle;
            if(angle > 0 && angle == angle){
                normal(prevMouseRad, curMouseRad, Tmp.v31);
                Core.graphics.setTitle(angle + "_" + curMouseRad);
                TestVars.renderer3D.cameraRotation.rotate(Tmp.v31, angle * 10f);

                cam.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
                cam.position.set(TestVars.renderer3D.cameraRotation).scl(TestVars.renderer3D.zoom).add(TestVars.renderer3D.cameraTarget);
                cam.lookAt(TestVars.renderer3D.cameraTarget);
                cam.update();


//        cam.unproject(curMouseRad.set(Core.graphics.getWidth()-Core.input.mouseX(),Core.graphics.getHeight()-Core.input.mouseY(), 0));
                cam.unproject(curMouseRad.set(Core.input.mouse(), 0));
                curMouseRad.nor();
            }
//            float deltaY = Core.input.mouseY() - prevMouse.y;
//            float deltaX = Core.input.mouseX() - prevMouse.x;
//            YRot = Mathf.clamp(YRot + deltaY, -90, 90);
//            XRot = Mathf.mod(XRot + deltaX, 360);
//            System.out.println(deltaY);
//            Vec3 vec3 = TestVars.renderer3D.cam.unproject(Tmp.v31.set(Core.input.mouse(), 0));
//            Core.graphics.setTitle(vec3 + "");
        }
        upd = false;
        prevMouseRad.set(curMouseRad);
//        TestVars.renderer3D.cameraRotation.set(1, 0, 0).rotate(Vec3.Y, XRot);
//        Tmp.v31.set(TestVars.renderer3D.cameraRotation).rotate(Vec3.Y, 90).y = 0;
//        Tmp.v31.nor();
//        TestVars.renderer3D.cameraRotation.rotate(Tmp.v31, YRot);


        Gl.clearColor(0f, 0f, 0f, 0f);
        Gl.clear(Gl.colorBufferBit);

        TestVars.renderer3D.render();
    }
}
