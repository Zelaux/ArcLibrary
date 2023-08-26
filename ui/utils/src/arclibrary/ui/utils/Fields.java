package arclibrary.ui.utils;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.*;
import arc.scene.ui.TextField.*;
import arc.scene.ui.layout.*;
import arc.scene.utils.*;
import arc.util.*;
import arclibrary.ui.defaults.DefaultBackground;
import kotlin.jvm.internal.Ref.*;


public class Fields{
    public static String tooBigNumberKey = "too-big-number";
    public static String numberOutOfRange = "number-out-of-range";

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

    public static TextField uniqueField(String def, String duplicateKey, Cons<String> listener, Boolf<String> validator){
        return uniqueField(def, duplicateKey, listener, validator, new ObjectRef<>());
    }

    public static TextField uniqueField(String def, String duplicateKey, Cons<String> listener, Boolf<String> validator, ObjectRef<String> tooltipText){
        String duplicateMessage = Core.bundle.get(duplicateKey);
        TextField textField = Elem.newField(def, listener);
        textField.setValidator(t -> tooltipText.element == null);
        textField.update(() -> {

            tooltipText.element = validator.get(textField.getText()) ? null : duplicateMessage;
        });
        return textField;
    }

    //region ranged number fields
    public static Cell<TextField> rangedNumberField(Table table, @Nullable String name, int defaultValue, int minValue, int maxValue, Intc consumer){
        return rangedNumberField(table, name == null ? null : () -> Core.bundle.get(name), defaultValue, minValue, maxValue, consumer);
    }

    public static Cell<TextField> rangedNumberField(Table table, @Nullable Prov<String> nameProv, int defaultValue, int minValue, int maxValue, Intc consumer){
        ObjectRef<TextField> fieldRef = new ObjectRef<>();
        ObjectRef<String> tooltipText = new ObjectRef<>();
        if(nameProv != null){
            table.label(nameProv::get);
        }
        return table.field(defaultValue + "", TextFieldFilter.digitsOnly, text -> {
            if(!Strings.canParseInt(text)){
                fieldRef.element.color.set(Color.scarlet);
                tooltipText.element = Core.bundle.get(tooBigNumberKey);
                return;
            }
            int number = Strings.parseInt(text);
            if(number > maxValue || number < minValue){
                fieldRef.element.color.set(Color.scarlet);
                tooltipText.element = Core.bundle.format(numberOutOfRange, minValue, maxValue);
                return;
            }
            fieldRef.element.color.set(Color.white);
            tooltipText.element = null;
            consumer.get(number);
        }).with(i -> fieldRef.element = i).tooltip(t -> {
            t.visible(() -> tooltipText.element != null);
            t.background(DefaultBackground.black6()).margin(4f).label(() -> tooltipText.element);
        });
    }

    public static Cell<TextField> rangedFloatNumberField(Table table, @Nullable String name, float defaultValue, float minValue, float maxValue, Floatc consumer){
        return rangedFloatNumberField(table, name == null ? null : () -> Core.bundle.get(name), defaultValue, minValue, maxValue, consumer);
    }

    public static Cell<TextField> rangedFloatNumberField(Table table, @Nullable Prov<String> nameProv, float defaultValue, float minValue, float maxValue, Floatc consumer){
        ObjectRef<TextField> fieldRef = new ObjectRef<>();
        ObjectRef<String> tooltipText = new ObjectRef<>();
        if(nameProv != null){
            table.label(nameProv::get);
        }
        return table.field(defaultValue % 1f == 0 ? ((int)defaultValue) + "" : defaultValue + "", TextFieldFilter.floatsOnly, text -> {
            if(!Strings.canParseFloat(text)){
                fieldRef.element.color.set(Color.scarlet);
                tooltipText.element = Core.bundle.get(tooBigNumberKey);
                return;
            }
            float number = Strings.parseFloat(text);
            if(number > maxValue || number < minValue){
                fieldRef.element.color.set(Color.scarlet);
                tooltipText.element = Core.bundle.format(numberOutOfRange, minValue, maxValue);
                return;
            }
            fieldRef.element.color.set(Color.white);
            tooltipText.element = null;
            consumer.get(number);
        }).with(i -> fieldRef.element = i).tooltip(t -> {
            t.visible(() -> tooltipText.element != null);
            t.background(DefaultBackground.black6()).margin(4f).label(() -> tooltipText.element);
        });
    }
    //endregion
}
