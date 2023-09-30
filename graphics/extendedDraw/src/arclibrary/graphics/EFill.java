package arclibrary.graphics;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;

import static arc.graphics.g2d.Fill.poly;

/** Extended Fill */
public class EFill{

    private static final Vec2 vector = new Vec2();
    private static final FloatSeq floats = new FloatSeq(20);
    private static final DonutEllipseDraw donutEllipseDraw = new DonutEllipseDraw();

    //region ellipse
    public static void polyCircle(float x, float y, float radius){
        pie(x, y, radius, 1f, 0f);
    }

    public static void pie(float x, float y, float radius, float finion, float rotation){
        ellipse(x, y, radius * 2f, radius * 2f, finion, 0, rotation);
    }

    public static void ellipse(float x, float y, float width, float height, float finion, float rotation){
        ellipse(x, y, width, height, finion, 0, rotation);
    }

    public static void ellipse(float x, float y, float width, float height, float finion, float angle, float rotation){
        donutEllipse(x, y, 0, 0, width, height, finion, angle, rotation);
       /* final float sides = 60;
        finion = Mathf.clamp(finion);
        int max = (int)(sides * (finion + 0.001F));
        vector.set(0F,0f);
        floats.clear();

        floats.add(x, y);
        Cons<Float> cons = (i) -> {
            float degrees = 360F / sides * i + angle;
            vector.set(1,0f).setAngle(degrees);
            vector.scl(width, height);
//            floats.add(Mathf.cos(degrees) * width + x, Mathf.sin(degrees) * height + y);
            floats.add(vector.x + x, vector.y + y);
        };
        int startI = 0;
        if(max % 2 != 0){
            startI = 1;
            cons.get(0f);
            cons.get(1f);
            cons.get(1f);
//            cons.get(2f);
        }
        for(float i = startI; i < (max); i += 2f){
            cons.get(i);
            cons.get(i + 1f);
            cons.get(i + 2f);
        }
        if(max > 0) poly(floats);
*/
    }

    public static void donut(float x, float y, float radius1, float radius2){
        donut(x, y, radius1, radius2, 1f);
    }

    public static void donut(float x, float y, float radius1, float radius2, float finion){
        donut(x, y, radius1, radius2, finion, 0f);
    }

    public static void donut(float x, float y, float radius1, float radius2, float finion, float rotation){
        donutEllipse(x, y, radius1 * 2, radius1 * 2, radius2 * 2, radius2 * 2, finion, 0f, rotation);
    }

    public static void donutEllipse(float x, float y, float width, float height, float width2, float height2, float finion, float rotation){
        donutEllipse(x, y, width, height, width2, height2, finion, 0f, rotation);
    }

    /**
     * @param rotation ellipse rotation
     * @param angleOffset ellipse start offset
     */
    public static void donutEllipse(float x, float y, float width, float height, float width2, float height2, float finion, float angleOffset, float rotation){
        donutEllipseDraw
            .set(x, y, width, height, width2, height2, finion, angleOffset, rotation)
            .donutEllipse();
    }

    private static void flushPoly(){
        poly(floats);
        floats.clear();
    }

    public static void tri(FloatSeq floats){
        if(floats.size < 6) return;
        float[] items = floats.items;
        Fill.tri(items[0], items[1], items[2], items[3], items[4], items[5]);
    }
    //endregion

    public static void quad(FloatSeq floats){
        if(floats.size < 8) return;
        float[] items = floats.items;
        Fill.quad(items[0], items[1], items[2], items[3], items[4], items[5], items[6], items[7]);
    }

    private static final class DonutEllipseDraw{
        final float sides = 50F;
        final float max = sides;
        float x, y, width, height, width2, height2, finion, angleOffset, rotation;

        public DonutEllipseDraw set(float x, float y, float width, float height, float width2, float height2, float finion, float angleOffset, float rotation){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.width2 = width2;
            this.height2 = height2;
            this.finion = finion;
            this.angleOffset = angleOffset;
            this.rotation = rotation;
            return this;
        }

        public void donutEllipse(){
            vector.set(0F, 0f);
            floats.clear();
            int startI = 0;
            //noinspection ConstantValue
            if(max % 2.0 != 0.0){
                startI = 1;
                apply1(0F);
                apply2(1f);
                flushPoly();
                apply1(1f);
                apply2(1f);
                flushPoly();
            }

            for(float i = (float)startI; i < max; i += 2f){
                apply1(i);
                apply2(i + 1f);
                flushPoly();
                apply1(i + 1f);
                apply2(i + 2f);
                flushPoly();
            }

        }

        private void apply2(float ix){
            adddPoints(360f * finion / max * ix, width2, height2, width, height);
        }

        private void adddPoints(float v, float startWidth, float startHeight, float endWidth, float endHeight){
            vector.set(0.5f, 0).setAngle(v + angleOffset).scl(startWidth, startHeight).rotate(rotation);
            floats.add(vector.x + x, vector.y + y);
            vector.set(0.5f, 0f).setAngle(v + angleOffset).scl(endWidth, endHeight).rotate(rotation);
            floats.add(vector.x + x, vector.y + y);
        }

        private void apply1(Float ix){
            adddPoints(360f * finion / max * ix, width, height, width2, height2);
        }
    }
}
