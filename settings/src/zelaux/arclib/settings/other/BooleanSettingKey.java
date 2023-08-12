//
// Decompiled by Procyon v0.5.36
//

package zelaux.arclib.settings.other;


import arc.Core;
import arc.func.Prov;
import zelaux.arclib.settings.SettingKey;


public class BooleanSettingKey extends SettingKey<Boolean> {
    public BooleanSettingKey(String key, Prov<Boolean> defaultProvider) {
        super(key, defaultProvider, BooleanSettingKey::get, BooleanSettingKey::set);
    }

    public static Boolean parseBooleanOrNull(String string) {
        return string.equals("true") ? Boolean.TRUE : (string.equals("false") ? Boolean.FALSE : null);
    }

    private static void set(SettingKey<Boolean> it, Boolean value) {
        Core.settings.put(it.key, value);
    }

    private static Boolean get(SettingKey<Boolean> it) {
        Boolean def = it.def();
        Boolean value = parseBooleanOrNull(Core.settings.get(it.key, def).toString());
        if (value != null) return value;
        Core.settings.put(it.key, def);
        return def;
    }
}
