//
// Decompiled by Procyon v0.5.36
//

package arclibrary.settings;

import arc.Core;
import arc.func.Cons2;
import arc.func.Func;
import arc.func.Prov;
import arc.struct.Seq;
import org.jetbrains.annotations.NotNull;


public class SettingKey<T> {


    @NotNull
    public static final Seq<SettingKey<?>> allKeys = new Seq<>();

    @NotNull
    public final String key;
    @NotNull
    private final Prov<T> defaultProvider;
    @NotNull
    private final Func<SettingKey<T>, T> valueGetter;
    @NotNull
    private final Cons2<SettingKey<T>, T> valueSetter;
    @NotNull
    private final Seq<Cons2<SettingKey<T>, T>> updateListeners;

    public SettingKey(@NotNull final String key, @NotNull final Prov<T> defaultProvider, @NotNull final Func<SettingKey<T>, T> valueGetter, @NotNull final Cons2<SettingKey<T>, T> valueSetter) {
        this.key = key;
        this.defaultProvider = defaultProvider;
        this.valueGetter = valueGetter;
        this.valueSetter = valueSetter;
        SettingKey.allKeys.add(this);
        this.updateListeners = new Seq<>();
    }

    @NotNull
    public final Prov<T> getDefaultProvider() {
        return this.defaultProvider;
    }

    @NotNull
    public final Seq<Cons2<SettingKey<T>, T>> getUpdateListeners() {
        return this.updateListeners;
    }

    public T def() {
        return (T) this.defaultProvider.get();
    }

    public final T get() {
        return (T) this.valueGetter.get(this);
    }

    public final void set(final T value) {
        this.valueSetter.get(this, value);
        for (Cons2<SettingKey<T>, T> listener : updateListeners) {
            listener.get(this, value);
        }
    }

    public void setDefault() {
        Core.settings.defaults(this.key, this.def());
    }
}
