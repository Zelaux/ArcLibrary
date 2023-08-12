//
// Decompiled by Procyon v0.5.36
//

package zelaux.arclib.settings.other;

import arc.Core;
import arc.func.Prov;
import arc.util.serialization.Jval;
import zelaux.arclib.settings.SettingKey;

public class JvalSettingKey extends SettingKey<Jval> {
    public JvalSettingKey(String key, Prov<Jval> defaultProvider) {
        super(key, defaultProvider, JvalSettingKey::get,  JvalSettingKey::set);
    }

    private static Jval get(final SettingKey<Jval> it) {
        final String str = Core.settings.get(it.key, it.def()).toString();

        Jval read = null;
        try {
            read = Jval.read(str);
        } catch (Exception ignored) {
        }
        if (read == null) {
            String string = it.def().toString(Jval.Jformat.formatted);
            Core.settings.put(it.key, string);
            read = Jval.read(string);
        }
        return read;
    }

    private static void set(final SettingKey<Jval> key, final Jval value) {
        Core.settings.put(key.key,  value.toString(Jval.Jformat.formatted));
    }

    @Override
    public void setDefault() {
        Core.settings.defaults(key, def().toString(Jval.Jformat.formatted));
    }
}
