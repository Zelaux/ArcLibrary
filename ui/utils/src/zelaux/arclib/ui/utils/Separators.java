package zelaux.arclib.ui.utils;

import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import zelaux.arclib.ui.defaults.*;

public class Separators{
    public static Drawable separatorDrawable;
    @SuppressWarnings("UnusedReturnValue")
    public static Cell<Image> verticalSeparator(Table table, Color color){
        return table.image(separatorDrawable(), color).growY().width(3f);
    }

    public static Cell<Image> horizontalSeparator(Table table, Color color){
        return table.image(separatorDrawable(), color).growX().height(3f);
    }

    private static Drawable separatorDrawable(){
        return separatorDrawable==null? DefaultBackground.white() :separatorDrawable;
    }
}
