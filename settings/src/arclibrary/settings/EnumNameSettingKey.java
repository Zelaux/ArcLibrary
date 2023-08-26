//
// Decompiled by Procyon v0.5.36
//

package arclibrary.settings;

import arc.Core;
import arc.func.Prov;
import org.jetbrains.annotations.NotNull;
import arc.struct.ObjectMap;


public class EnumNameSettingKey<T extends Enum<T>> extends SettingKey<T> {
    @NotNull
    private final ObjectMap<String, T> map;

    public EnumNameSettingKey( String key,  T[] values,  Prov<T> defaultProvider) {
        super(key,defaultProvider,EnumNameSettingKey::get,EnumNameSettingKey::set);
        map=new ObjectMap<>();
        for (T value : values) {
            map.put(value.name(),value);
        }
    }

    @NotNull
    public final ObjectMap<String, T> getMap() {
        return this.map;
    }

    @Override
    public void setDefault() {
        Core.settings.defaults(this.key, this.def().name());
    }

    private static <T extends Enum<T>> T get(SettingKey<T> it) {
        ObjectMap<String, T> map = ((EnumNameSettingKey<T>)it).map;

        String string = Core.settings.getString(it.key, null);
        T element=null;
        if(string!=null){
            element=map.get(string);
        }
        if(element!=null)return element;
        final T def = it.def();
        set(it,def);
        return def;
    }

    private static <T extends Enum<T>> void set( SettingKey<T> key,T value) {
        Core.settings.put(key.key, value.name());
    }
}
