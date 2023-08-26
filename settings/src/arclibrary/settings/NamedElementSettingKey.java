//
// Decompiled by Procyon v0.5.36
//

package arclibrary.settings;

import arc.Core;
import arc.func.Prov;
import arc.struct.ObjectMap;
import org.jetbrains.annotations.NotNull;


public class NamedElementSettingKey<T extends NamedElementSettingKey.NamedElement> extends SettingKey<T> {
    @NotNull
    private final ObjectMap<String, T> map;

    public NamedElementSettingKey(String key, T[] values, Prov<T> defaultProvider) {
        super(key, defaultProvider, NamedElementSettingKey::get, NamedElementSettingKey::set);
        map = new ObjectMap<>();
        for (T value : values) {
            map.put(value.settingsKey(), value);
        }
    }

    private static <T extends NamedElement> T get(SettingKey<T> it) {
        ObjectMap<String, T> map = ((NamedElementSettingKey<T>) it).map;

        String string = Core.settings.getString(it.key, null);
        T element = null;
        if (string != null) {
            element = map.get(string);
        }
        if (element != null) return element;
        final T def = it.def();
        set(it, def);
        return def;
    }

    private static <T extends NamedElement> void set(SettingKey<T> key, T value) {
        Core.settings.put(key.key, value.settingsKey());
    }

    @NotNull
    public final ObjectMap<String, T> getMap() {
        return this.map;
    }

    @Override
    public void setDefault() {
        Core.settings.defaults(this.key, this.def().settingsKey());
    }

    public interface NamedElement {
        String settingsKey();
    }
}
