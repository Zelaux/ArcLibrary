package arclibrary.settings;

import arc.func.*;
import arc.struct.*;
import org.jetbrains.annotations.*;

public class SettingKeyGroup{
    public static final SettingKeyGroup zeroGroup = new SettingKeyGroup(null);
    public static SettingKeyGroup defaultGroup = zeroGroup;
    public final String name;
    @NotNull
    protected final Seq<SettingKey<?>> myKeys = new Seq<>();

    public SettingKeyGroup(String name){
        this.name = name;
    }

    public boolean registerKey(SettingKey<?> key){
        if(myKeys.contains(key)){
            return false;
        }
        myKeys.add(key);
        return true;
    }

    public int keysAmount(){
        return myKeys.size;
    }

    public boolean unregisterKey(SettingKey<?> key){
        return myKeys.remove(key);
    }

    public SettingKey<?> getAt(int index){
        return myKeys.get(index);
    }

    public <T> T collectKeysTo(T collector, Cons2<T, SettingKey<?>> collectFunction){
        for(int i = 0; i < myKeys.size; i++){
            collectFunction.get(collector, myKeys.get(i));
        }
        return collector;
    }

    public void eachKey(Cons<SettingKey<?>> collectFunction){
        myKeys.each(collectFunction);
    }

}
