//
// Decompiled by Procyon v0.5.36
//

package arclibrary.settings.other;

import arc.Core;
import arc.func.Prov;
import arc.graphics.Color;
import arclibrary.settings.SettingKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ColorSettingKey extends SettingKey<Color> {
    final Color tmpColor;

    public ColorSettingKey(@NotNull String key, @Nullable Color tmpColor, @NotNull Prov<Color> defaultProvider) {
        super(key, defaultProvider, ColorSettingKey::get, ColorSettingKey::set);
        this.tmpColor = tmpColor;
    }

    private static Color get(SettingKey<Color> it) {
        Color tmpColor = ((ColorSettingKey) it).tmpColor;
        int rgba8888 = Core.settings.getInt(it.key, it.def().rgba());
        if (tmpColor == null) {
            return new Color(rgba8888);
        } else {
            return tmpColor.set(rgba8888);
        }
    }

    private static void set(SettingKey<Color> key, Color value) {
        Core.settings.put(key.key, value.rgba());
    }

    @Override
    public void setDefault() {
        Core.settings.defaults(this.key, this.def().rgba());
    }
}
