package arclibrary.utils.events;

import arc.*;
import arc.struct.*;
import arc.util.*;
import lombok.*;
import org.jetbrains.annotations.*;

import java.lang.reflect.*;
import java.util.*;

public class EventSender<T>{
    public final String commandName;
    public final EventSignature<T> signature;
    private final ObjectMap<String, Object> parametersMap = new ObjectMap<>();

    public EventSender(EventReceiver<T> receiver){
        this(receiver.commandName, receiver.signature);
    }

    public EventSender(String commandName, @NotNull EventSignature<T> signature){
        this.commandName = commandName;
        this.signature = Objects.requireNonNull(signature);
    }

    public static <T> EventSender<T> from(EventReceiver<T> sender){
        return new EventSender<>(sender);
    }

    public <P> void setParameter(String name, P parameter){
        parametersMap.put(name, parameter);
    }

    @SuppressWarnings("unused")
    @SneakyThrows
    public void setParameters(@NotNull T object){
        Objects.requireNonNull(object, "object cannot be null");
        //noinspection UnnecessaryLocalVariable
        Class<T> signatureClass = signature.targetClass();
        Class<?> c = signatureClass;
        while(c != null){
            Field[] fields = c.getDeclaredFields();
            for(Field field : fields){
                int modifiers = field.getModifiers();
                if(!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) continue;
                field.setAccessible(true);
                setParameter(field.getName(), field.get(object));
            }
            c = c.getSuperclass();
        }
    }

    public void clear(){
        parametersMap.clear();
    }

    public void fire(){
        fire(false);
    }

    public void fire(boolean clearParams){
        Seq<Object> objects = new Seq<>();
        objects.add(commandName);
        if(signature != null){
            objects.add(EventSignature.eventSignatureParam);
            objects.add((Object)signature.toObjects());
        }
        for(ObjectMap.Entry<String, Object> entry : parametersMap){
            objects.add(entry.key, entry.value);
        }
        Events.fire(objects.toArray(Object.class));
        if(clearParams){
            clear();
        }
    }
}
