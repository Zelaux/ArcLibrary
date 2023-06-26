package tests;

import arc.ApplicationCore;
import arc.ApplicationListener;
import arc.Core;
import arc.Files;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.files.Fi;
import arc.graphics.Gl;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.util.Log;
import arc.util.Time;
import zelaux.arclib.graphics.g3d.model.obj.ObjectModelFactory;
import zelaux.arclib.graphics.g3d.model.obj.ObjectShader;
import zelaux.arclib.graphics.g3d.render.GenericRenderer3D;

public class Tests extends ApplicationCore {
    public static void main(String[] args) {
        new SdlApplication(new Tests(), new SdlConfig(){{
            gl30 = true;
            title = "G3D test";
            maximized = true;
        }});
    }

    @Override
    public void setup() {
        Time.mark();



        Vars.renderer3D = new GenericRenderer3D();
        Vars.renderer3D.init();
        Vars.renderer3D.models.addAll(ObjectModelFactory.create(new Fi("models/suz/suz.obj", Files.FileType.internal),
                new ObjectShader(new Fi("shaders/objecttype/shader.vert", Files.FileType.internal),
                        new Fi("shaders/objecttype/shader.frag", Files.FileType.internal))));

        Core.batch = new SortedSpriteBatch();

        Log.info("[Tests] Setup time: @ms", Time.elapsed());
    }

    @Override
    public void update() {
        Gl.clearColor(0f, 0f, 0f, 0f);
        Gl.clear(Gl.colorBufferBit);

        Vars.renderer3D.render();
    }
}