package arclibrary.ui.utils;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.TextField.*;
import arc.scene.ui.layout.*;
import arc.scene.utils.*;
import arc.util.*;
import kotlin.jvm.internal.Ref.*;
import zelaux.arclib.ui.defaults.*;

public class UIUtils{
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



    public static int invertAlign(int align){
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
}
