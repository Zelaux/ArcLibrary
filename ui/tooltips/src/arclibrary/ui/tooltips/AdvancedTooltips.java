package arclibrary.ui.tooltips;

import arc.func.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arclibrary.ui.defaults.*;
import arclibrary.ui.utils.UIUtils.*;
import arclibrary.utils.refs.Ref.*;
import org.intellij.lang.annotations.*;
import org.jetbrains.annotations.*;
import org.jetbrains.annotations.Nullable;

public class AdvancedTooltips{
    public static Cons2<String, Table> defaultTooltipTable = (text, table) -> {
        table.background(DefaultBackground.black6()).margin(4f).add(text);
    };

    public static <T extends Element> Cell<T> mutableTooltip(Table table, T element, ObjectRef<@Nullable String> tooltipText){
        return table.add(element).tooltip(it ->
            it.background(DefaultBackground.black6())
                .margin(4)
                .label(() -> tooltipText.element)
                .visible(() -> tooltipText.element != null)
        );
    }

    public static SideTooltip create(String text){
        return create(Align.topRight, text);
    }

    public static SideTooltip create(@AlignConstant int align, String text){
        return new SideTooltip(align, it -> defaultTooltipTable.get(text, it));
    }

    @NotNull
    public static <T extends Element> Cell<T> tooltipSide(Cell<T> self, @AlignConstant int align, String tooltip){
        return tooltipSide(self, align, it -> defaultTooltipTable.get(tooltip, it));
    }

    public static <T extends Element> Cell<T> tooltipSide(Cell<T> self, @AlignConstant int align, Cons<Table> builder){
        self.get().addListener(new SideTooltip(align, builder));
        return self;
    }
}
