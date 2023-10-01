package arclibrary.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;

/** Extended Draw */
public class EDraw{
    private static float[] vertices = new float[24];

    public static void quad(TextureRegion region, float x1, float y1, float c1, float x2, float y2, float c2, float x3, float y3, float c3, float x4, float y4, float c4){
        float u = region.u, v = region.v, u2 = region.u2, v2 = region.v2;
        quad(region.texture,
            x1, y1, c1, u, v,
            x2, y2, c2, u, v2,
            x3, y3, c3, u2, v2,
            x4, y4, c4, u2, v
        );
    }

    public static void rectCenter(Texture texture, float x, float y, float width, float height){
        rect(texture, x - width / 2f, y - height / 2, width, height);
    }

    public static void drawBuffer(FrameBuffer buffer, float centerX, float centerY){
        drawBuffer(buffer, centerX, centerY, Core.camera.width, -Core.camera.height);
    }

    public static void drawBuffer(FrameBuffer buffer, float centerX, float centerY, float width, float height){
        rectCenter(buffer.getTexture(), centerX, centerY, width, height);
    }

    public static void drawBuffer(FrameBuffer buffer){
        drawBuffer(buffer, Core.camera.position.x, Core.camera.position.y);
    }

    public static void rect(Texture texture, float x, float y, float width, float height){
        float color = Draw.getColor().toFloatBits();
        quad(texture,
            x, y, color, 0, 0,
            x, y + height, color, 0, 1,
            x + width, y + height, color, 1, 1,
            x + width, y, color, 1, 0
        );
    }

    public static void quad(Texture texture,
                            float x1, float y1, float c1, float u1, float v1,
                            float x2, float y2, float c2, float u2, float v2,
                            float x3, float y3, float c3, float u3, float v3,
                            float x4, float y4, float c4, float u4, float v4
    ){
        float mcolor = Draw.getMixColor().toFloatBits();
        vertices[0] = x1;
        vertices[1] = y1;
        vertices[2] = c1;
        vertices[3] = u1;
        vertices[4] = v1;
        vertices[5] = mcolor;

        vertices[6] = x2;
        vertices[7] = y2;
        vertices[8] = c2;
        vertices[9] = u2;
        vertices[10] = v2;
        vertices[11] = mcolor;

        vertices[12] = x3;
        vertices[13] = y3;
        vertices[14] = c3;
        vertices[15] = u3;
        vertices[16] = v3;
        vertices[17] = mcolor;

        vertices[18] = x4;
        vertices[19] = y4;
        vertices[20] = c4;
        vertices[21] = u4;
        vertices[22] = v4;
        vertices[23] = mcolor;

        Draw.vert(texture, vertices, 0, vertices.length);
    }
}
