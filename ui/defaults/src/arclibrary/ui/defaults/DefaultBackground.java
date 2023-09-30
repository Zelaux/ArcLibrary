package arclibrary.ui.defaults;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.scene.style.*;

public class DefaultBackground{
    private static final Lazy<Drawable> white = new Lazy<>(() -> {
        Pixmap pixmap = new Pixmap(1, 1);
        pixmap.set(0, 0, Color.whiteRgba);
        return new TextureRegionDrawable(Core.atlas.white());
    });
    private static final Lazy<Drawable> black6 = new Lazy<>(() ->
        new TextureRegionDrawable(Core.atlas.white()).tint(0, 0, 0, 0.6f));

    public static Drawable white(){
        return white.get();
    }

    public static Drawable black6(){
        return black6.get();
    }

    public static void white(Drawable value){
        white.set(value);
    }

    public static void black6(Drawable value){
        black6.set(value);
    }
}

class Lazy<T>{
    private static final Object UNINITIALIZED_VALUE = new Object();
    private final Object lock = new Object();
    private Prov<T> initializer;
    private Object _value = UNINITIALIZED_VALUE;

    Lazy(Prov<T> initializer){
        this.initializer = initializer;
    }

    public void set(T value){
        _value = value;
    }

    public T get(){
        Object _v1 = _value;
        if(_v1 != UNINITIALIZED_VALUE){
            //noinspection unchecked
            return (T)_v1;
        }
        synchronized(lock){
            Object _v2 = _value;
            if(_v2 != UNINITIALIZED_VALUE){
                //noinspection unchecked
                return (T)_v2;
            }
            T typedValue = initializer.get();
            _value = typedValue;
            initializer = null;
            return typedValue;
        }

    }
}
