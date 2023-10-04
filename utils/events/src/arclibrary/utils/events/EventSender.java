package arclibrary.utils.events;

import arc.*;
import arc.struct.*;
import org.jetbrains.annotations.*;

public class EventSender{
    public final String commandName;
    @Nullable
    public final EventSignature signature;
    private final ObjectMap<String, Object> parametersMap = new ObjectMap<>();

    public EventSender(EventReceiver receiver){
        this(receiver.commandName, receiver.signature);
    }

    public EventSender(String commandName, @Nullable EventSignature signature){
        this.commandName = commandName;
        this.signature = signature;
    }

    @Deprecated
    public EventSender(String commandName){
        this.commandName = commandName;
        this.signature = null;
    }

    public <T> void setParameter(String name, T parameter){
        parametersMap.put(name, parameter);
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
