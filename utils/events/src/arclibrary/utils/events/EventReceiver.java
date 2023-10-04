package arclibrary.utils.events;

import arc.*;
import arc.func.*;
import arc.struct.*;

public class EventReceiver{
    public final String commandName;
    public final EventSignature signature;
    private final ObjectMap<String, Object> parametersMap = new ObjectMap<>();

    public EventReceiver(EventSender sender){
        this(sender.commandName, sender.signature);
    }

    public EventReceiver(String commandName, EventSignature signature){
        this.commandName = commandName;
        this.signature = signature;
    }

    @Deprecated
    public EventReceiver(String commandName){
        this.commandName = commandName;
        signature = null;
    }

    public int paramsAmount(){
        if(signature != null) return signature.amount();
        return parametersMap.size;
    }

    public boolean set(Object[] objects){
        parametersMap.clear();
        if(!commandName.equals(objects[0]) || (objects.length - 1) % 2 != 0){
            return false;
        }
        boolean valid = true;
        for(int i = 1; i < objects.length - 1 && valid; i += 2){
            valid = objects[i] instanceof String;
            if(valid) valid = objects.length - 1 > i;
        }
        if(!valid){
            return false;
        }
        parametersMap.clear();
        for(int i = 1; i < objects.length; i += 2){
            parametersMap.put((String)objects[i], objects[i + 1]);
        }
        if(parametersMap.containsKey(EventSignature.eventSignatureParam) && signature != null){
            Object o = parametersMap.get(EventSignature.eventSignatureParam);
            if(!(o instanceof Object[])) return false;
            Object[] inputSignature = (Object[])o;
            if(!signature.matches(inputSignature)) return false;

        }
        parametersMap.remove(EventSignature.eventSignatureParam);
        return true;
    }

    public boolean hasParameter(String name, Class<?> type){
        if(!parametersMap.containsKey(name)) return false;
        Object object = parametersMap.get(name);
        return (type == null || type.isInstance(object));
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String name){
        return (T)parametersMap.getNull(name);
    }

    public Number getNumParam(String name){
        return getParameter(name);
    }

    public String getStringParam(String name){
        return getParameter(name);
    }

    public void post(Cons<EventReceiver> cons){
        Events.on(Object[].class, objects1 -> {
            if(set(objects1)){
                try{
                    cons.get(this);
                }catch(Exception exception){
                    exception.printStackTrace();
                }
                parametersMap.clear();
            }
        });
    }

    private static class UnknownObjects extends Exception{

    }
}
