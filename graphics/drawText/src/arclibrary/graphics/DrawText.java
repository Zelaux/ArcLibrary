package arclibrary.graphics;

import arc.*;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.FontCache;
import arc.graphics.g2d.GlyphLayout;
import arc.math.geom.Position;
import arc.scene.ui.layout.Scl;
import arc.struct.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DrawText{
    @Nullable
    public static Font defaultFont;
    private static Seq<Font> fontList;
    /**
     * {@link DrawText#defaultFont} must be set
     * */
    public static void drawText(float x, float y, float textSize, @NotNull Color color, @NotNull String text){
        drawText(defaultFont(), x, y, textSize, color, text);
    }

    @NotNull
    private static Font defaultFont(){
        if(defaultFont == null && Core.assets != null && fontList == null){
            fontList = new Seq<>();
            defaultFont = Core.assets.getAll(Font.class, fontList).firstOpt();
        }
        return Objects.requireNonNull(defaultFont,"arclibrary.graphics.DrawText.defaultFont is null");
    }

    public static void drawText(@NotNull Font font, float x, float y, float textSize, @NotNull Color color, @NotNull String text){
        boolean ints = font.usesIntegerPositions();
        font.getData().setScale(textSize / Scl.scl(1.0f));
        font.setUseIntegerPositions(false);

        font.setColor(color);

        float z = Draw.z();
        Draw.z(z + 0.01f);
        FontCache cache = font.getCache();
        cache.clear();
        GlyphLayout layout = cache.addText(text, x, y);

        font.draw(text, x - layout.width / 2f, y + layout.height / 2f);
        Draw.z(z);

        font.setUseIntegerPositions(ints);
        font.getData().setScale(1);
    }
    /**
     * {@link DrawText#defaultFont} must be set
     * */
    public static void drawText(@NotNull Position pos, float textSize, @NotNull String text){
        drawText(defaultFont(), pos, textSize, text);
    }

    public static void drawText(@NotNull Font font, @NotNull Position pos, float textSize, @NotNull String text){
        drawText(font, pos.getX(), pos.getY(), textSize, Color.white, text);
    }
    /**
     * {@link DrawText#defaultFont} must be set
     * */
    public static void drawText(@NotNull Position pos, @NotNull Color color, @NotNull String text){
        drawText(defaultFont(), pos, color, text);
    }

    public static void drawText(@NotNull Font font, @NotNull Position pos, @NotNull Color color, @NotNull String text){
        drawText(font, pos.getX(), pos.getY(), 0.23f, color, text);
    }
    /**
     * {@link DrawText#defaultFont} must be set
     * */
    public static void drawText(@NotNull Position pos, @NotNull String text){
        drawText(defaultFont(), pos, text);
    }

    public static void drawText(@NotNull Font font, @NotNull Position pos, @NotNull String text){
        drawText(font, pos, Color.white, text);
    }
    /**
     * {@link DrawText#defaultFont} must be set
     * */
    public static void drawText(float x, float y, float textSize, @NotNull String text){
        drawText(defaultFont(), x, y, textSize, text);
    }

    public static void drawText(@NotNull Font font, float x, float y, float textSize, @NotNull String text){
        drawText(font, x, y, textSize, Color.white, text);
    }
    /**
     * {@link DrawText#defaultFont} must be set
     * */
    public static void drawText(float x, float y, @NotNull Color color, @NotNull String text){
        drawText(defaultFont(), x, y, color, text);
    }

    public static void drawText(@NotNull Font font, float x, float y, @NotNull Color color, @NotNull String text){
        drawText(font, x, y, 0.23f, color, text);
    }
    /**
     * {@link DrawText#defaultFont} must be set
     * */
    public static void drawText(float x, float y, @NotNull String text){
        drawText(defaultFont(), x, y, text);
    }

    public static void drawText(@NotNull Font font, float x, float y, @NotNull String text){
        drawText(font, x, y, Color.white, text);
    }
}
