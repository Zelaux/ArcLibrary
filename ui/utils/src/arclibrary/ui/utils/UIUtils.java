package arclibrary.ui.utils;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.util.*;
import org.intellij.lang.annotations.MagicConstant;


public class UIUtils{
    /**
     * Adds '\n' if text not within maxWidth
     * */
    public static String wrapText(String originalString, Font font, float maxWidth){

        GlyphLayout obtain = GlyphLayout.obtain();

        obtain.setText(font, originalString);
        if(obtain.width <= maxWidth){
            obtain.free();
            return originalString;
        }
        String[] words = originalString.split(" ");
        StringBuilder builder = new StringBuilder();
        int wordIndex = 0;
        while(wordIndex < words.length){
            builder.append(words[wordIndex]);
            if(wordIndex + 1 == words.length){
                break;
            }
            obtain.setText(font, builder + " " + words[wordIndex + 1]);
            if(obtain.width <= maxWidth){
                builder.append(" ");
            }else{
                builder.append("\n");
            }
            wordIndex++;
        }
        obtain.free();
        return builder.toString();
    }
    @Nullable
    public static Element hovered(Boolf<Element> validator){
        Element e = Core.scene.hit(Core.input.mouseX(), Core.input.mouseY(), true);
        if(e != null){
            while(e != null && !validator.get(e)){
                e = e.parent;
            }
        }
        return e;
        /*
        if(e == null || isDescendantOf(e)) return null;
        return (StatementElem)e;*/
    }

    public static <T extends Element> T hitChild(Group group, float stageX, float stageY, @Nullable Boolf<Element> filter){
        //noinspection unchecked
        return (T)group.getChildren().find(it -> {
            if(filter != null && !filter.get(it)){
                return false;
            }

            it.stageToLocalCoordinates(Tmp.v1.set(stageX, stageY));
            return it.hit(Tmp.v1.x, Tmp.v1.y, false) != null;
        });
    }


    @AlignConstant
    public static int invertAlign(@AlignConstant int align){
        int result = align & Align.center;

        if((align & Align.left) != 0){
            result |= Align.right;
        }
        if((align & Align.right) != 0){
            result |= Align.left;
        }
        if((align & Align.top) != 0){
            result |= Align.bottom;
        }
        if((align & Align.bottom) != 0){
            result |= Align.top;
        }
        return result;
    }


    public static float getX(float x, float width, int align){
        float offset = 0;
        if((align & Align.right) != 0){
            offset = width;
        }
        if((align & Align.center) != 0){
            offset = width / 2f;
        }
        return x + offset;
    }

    public static float getY(float y, float height, int align){
        float offset = 0;
        if((align & Align.top) != 0){
            offset = height;
        }
        if((align & Align.center) != 0){
            offset = height / 2f;
        }
        return y + offset;
    }

    public static void replaceClickListener(Button button, ClickListener newListener){
        button.removeListener(button.getClickListener());

        Reflect.set(Button.class, button, "clickListener", newListener);
        button.addListener(newListener);

    }

    @MagicConstant(flagsFromClass = Align.class)
    public @interface AlignConstant{

    }
}
