//
// Decompiled by Procyon v0.5.36
//

package zelaux.arclib.settings;

import arc.Core;
import arc.func.Prov;


public class EnumIdSettingKey<T extends Enum<T>> extends SettingKey<T> {
    private final T[] values;

    public EnumIdSettingKey(String key, T[] values, Prov<T> defaultProvider) {
        super(key, defaultProvider, EnumIdSettingKey::get, EnumIdSettingKey::set);
        this.values = values;
    }

    private static <T extends Enum<T>> T get(SettingKey<T> it) {
        T[] values = ((EnumIdSettingKey<T>) it).values;
        return values[Core.settings.getInt(it.key, it.def().ordinal())];
    }

    private static <T extends Enum<T>> void set(SettingKey<T> key, T value) {
        Core.settings.put(key.key, value.ordinal());
    }

    @Override
    public void setDefault() {
        Core.settings.defaults(key, def().ordinal());
    }
}
