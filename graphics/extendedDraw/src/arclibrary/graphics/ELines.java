package arclibrary.graphics;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;

/** Extended Lines */
public class ELines{
    private static final FloatSeq floats = new FloatSeq(20);
    private static final Vec2 tmp1 = new Vec2();
    private static final float[] rectPoints = {
        0, 0,
        1, 0,
        1, 1,
        0, 0
    };

    public static void arc(float x, float y, float radius, float finion){
        arc(x, y, radius, finion, 0.0F);
    }

    public static void arc(float x, float y, float radius, float finion, float angle){
        float stroke = Lines.getStroke();
        float halfStroke = stroke / 2.0F;
        EFill.donut(x, y, radius - halfStroke, radius + halfStroke, finion, angle);
    }

    //region rect
    public static void square(float x, float y, float rad){
        rect(x - rad, y - rad, rad * 2.0F, rad * 2.0F);
    }

    public static void rect(float x, float y, float width, float height, float originX, float originY, float rotation){
        float stroke = Lines.getStroke();
        float doubleStroke = stroke * 2f;
        for(int i = 0; i < 4; i++){
            int nextI = (i + 1) % 4;
            floats.clear();
            rectCorner(i, x, y, width, height, originX, originY, rotation);
            rectCorner(i, x, y, width - doubleStroke, height - doubleStroke, originX, originY, rotation);
            rectCorner(nextI, x, y, width - doubleStroke, height - doubleStroke, originX, originY, rotation);
            rectCorner(nextI, x, y, width, height, originX, originY, rotation);
            EFill.quad(floats);
        }
        floats.clear();
    }

    private static void rectCorner(int i, float x, float y, float width, float height, float originX, float originY, float rotation){
        tmp1.set(rectPoints[i * 2], rectPoints[i * 2 + 1]).scl(width, height)
            .sub(originX, originY)
            .rotate(rotation)
            .add(originX + x, originY + y);
        floats.add(tmp1.x, tmp1.y);
    }

    public static void rect(float x, float y, float width, float height){
        rect(x, y, width, height, 0);
    }

    public static void rect(float x, float y, float width, float height, float rot){
        rect(x, y, width, height, width / 2f, height / 2f, rot);
    }

    public static void rect(Rect rect){
        rect(rect.x, rect.y, rect.width, rect.height, 0);
    }

//endregion
}
