//
// Decompiled by Procyon v0.5.36
//

package zelaux.arclib.settings.other;

import arc.Core;
import arc.func.Prov;
import zelaux.arclib.settings.SettingKey;


public class StringSettingKey extends SettingKey<String> {
    public StringSettingKey( String key,  Prov<String> defaultProvider) {
        super(key, defaultProvider, StringSettingKey::get, StringSettingKey::set);
    }

    private static  String get(final SettingKey<String> it) {
        return Core.settings.get(it.key, it.def()).toString();
    }

    private static  void set( SettingKey<String> key, String value) {
        Core.settings.put(key.key, value);
    }
}
