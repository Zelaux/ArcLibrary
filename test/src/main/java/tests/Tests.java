package tests;

import arc.ApplicationCore;
import arc.Core;
import arc.Files;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.files.Fi;
import arc.graphics.Gl;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.input.*;
import arc.math.*;
import arc.util.Log;
import arc.util.Time;
import tests.core.*;
import zelaux.arclib.graphics.g3d.model.obj.ObjectModelFactory;
import zelaux.arclib.graphics.g3d.model.obj.ObjectShader;
import zelaux.arclib.graphics.g3d.render.GenericRenderer3D;

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

    @Override
    public void setup(){
        Time.mark();

        TestVars.renderer3D = new TestRenderer3D();
        TestVars.renderer3D.init();
        try{
            TestVars.renderer3D.models.addAll(ObjectModelFactory.create(new Fi("models/suz/suz.obj", Files.FileType.internal),
            new ObjectShader(new Fi("shaders/objecttype/shader.vert", Files.FileType.internal),
            new Fi("shaders/objecttype/shader.frag", Files.FileType.internal))));
        }catch(IOException e){
            e.printStackTrace();
        }

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
            public boolean scrolled(float amountX, float amountY){
                TestVars.renderer3D.zoom = Mathf.clamp(TestVars.renderer3D.zoom + amountY * 0.1f * Mathf.clamp(TestVars.renderer3D.zoom, 0.1f, 10000f), 0.1f, 10000f);
                return false;
            }
        });
    }

    @Override
    public void update(){
        Gl.clearColor(0f, 0f, 0f, 0f);
        Gl.clear(Gl.colorBufferBit);

        TestVars.renderer3D.render();
    }
}
