//
// Decompiled by Procyon v0.5.36
//

package arclibrary.settings;

import arc.Core;
import arc.func.Cons2;
import arc.func.Func;
import arc.func.Prov;
import arc.struct.Seq;
import arclibrary.settings.number.*;
import arclibrary.settings.other.*;
import org.jetbrains.annotations.NotNull;

/**
 * Way to reduce string constants from code and make refactoring easier<br/>
 * Example:<br/>
 * Define SettingKeys<br/>
 * <pre>
 * {@code
 * interface SettingKeys{
 *     IntSettingKey myFavoriteNumber=new IntSettingKey("my-favorite-number",()->6);
 * }
 * }
 * </pre><br/>
 * Then you can use <pre>{@code SettingKeys.myFavoriteNumber.get()}</pre>
 * instead of using
 * <pre>{@code arc.Core.settings.getInt("my-favorite-number",6)}</pre>
 *
 * <br>
 * Number keys:
 * <ul>
 * <li>{@link DoubleSettingKey}</li>
 * <li>{@link FloatSettingKey}</li>
 * <li>{@link IntSettingKey}</li>
 * <li>{@link LongSettingKey}</li>
 * </ul>
 * Java keys:<br>
 * <ul>
 * <li>{@link BooleanSettingKey}</li>
 * <li>{@link StringSettingKey}</li>
 * <li>{@link EnumIdSettingKey}</li>
 * <li>{@link EnumNameSettingKey}</li>
 * </ul>
 * Other keys:<br>
 * <ul>
 * <li>{@link ColorSettingKey}</li>
 * <li>{@link JvalSettingKey}</li>
 * <li>{@link NamedElementSettingKey}</li>
 * </ul>
 * */
public class SettingKey<T>{
    @NotNull
    public final String key;
    @NotNull
    public final SettingKeyGroup group;
    @NotNull
    private final Prov<T> defaultProvider;
    @NotNull
    private final Func<SettingKey<T>, T> valueGetter;
    @NotNull
    private final Cons2<SettingKey<T>, T> valueSetter;
    @NotNull
    private final Seq<Cons2<SettingKey<T>, T>> updateListeners;

    public SettingKey(
        @NotNull final SettingKeyGroup group,
        @NotNull final String key,
        @NotNull final Prov<T> defaultProvider,
        @NotNull final Func<SettingKey<T>, T> valueGetter,
        @NotNull final Cons2<SettingKey<T>, T> valueSetter){
        this.key = key;
        this.defaultProvider = defaultProvider;
        this.valueGetter = valueGetter;
        this.valueSetter = valueSetter;
        group.registerKey(this);
        this.group = group;
        this.updateListeners = new Seq<>();
    }

    public SettingKey(@NotNull final String key,
                      @NotNull final Prov<T> defaultProvider,
                      @NotNull final Func<SettingKey<T>, T> valueGetter,
                      @NotNull final Cons2<SettingKey<T>, T> valueSetter){
        this(SettingKeyGroup.defaultGroup, key, defaultProvider, valueGetter, valueSetter);
    }

    @NotNull
    public final Prov<T> getDefaultProvider(){
        return this.defaultProvider;
    }

    @NotNull
    public final Seq<Cons2<SettingKey<T>, T>> getUpdateListeners(){
        return this.updateListeners;
    }

    public T def(){
        return (T)this.defaultProvider.get();
    }

    public final T get(){
        return (T)this.valueGetter.get(this);
    }

    public final void set(final T value){
        this.valueSetter.get(this, value);
        for(Cons2<SettingKey<T>, T> listener : updateListeners){
            listener.get(this, value);
        }
    }

    public void setDefault(){
        Core.settings.defaults(this.key, this.def());
    }
}
