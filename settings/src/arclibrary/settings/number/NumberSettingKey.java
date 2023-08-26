package arclibrary.settings.number;

import arc.Core;
import arc.func.Func;
import arc.func.Prov;
import arclibrary.settings.SettingKey;

public abstract class NumberSettingKey<T extends Number> extends SettingKey<T> {
    private final Func<String, T> parser;

    NumberSettingKey(String key, Func<String, T> parser, Prov<T> defaultProvider) {
        super(key, defaultProvider, NumberSettingKey::get, NumberSettingKey::set);
        this.parser = parser;
    }

    private static <T extends Number> T get(SettingKey<T> it) {
        Func<String, T> parser = ((NumberSettingKey<T>) it).parser;
        String string = String.valueOf(Core.settings.get(it.key, null));
        T number = null;
        if (string != null) {
            try {
                number = parser.get(string);
            } catch (Exception ignored) {
            }
        }
        if (number != null) return number;
        T def = it.def();
        Core.settings.put(it.key, def);
        return def;
    }

    private static void set(SettingKey<?> key, Number value) {
        Core.settings.put(key.key, value);
    }
}
