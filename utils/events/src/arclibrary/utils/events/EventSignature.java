package arclibrary.utils.events;

import arc.struct.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;

public class EventSignature{
    public static final String eventSignatureParam = "_\n_EVENT_SIGNATURE__";
    private static final ThreadLocal<Bits> bits = ThreadLocal.withInitial(Bits::new);
    final Seq<Parameter> parameters = new Seq<>();

    public static EventSignature empty(){
        return new EventSignature();
    }

    public static EventSignature make(String name, Class<?> type){
        return new EventSignature().with(name, type);
    }

    public EventSignature with(String name, Class<?> type){
        return required(name, type);
    }

    public EventSignature required(String name, Class<?> type){
        return with(name, type, true);
    }

    public EventSignature optional(String name, Class<?> type){
        return with(name, type, false);
    }

    public EventSignature with(String name, Class<?> type, boolean required){
        parameters.add(new Parameter(name, required, type));
        return this;
    }

    public Object[] toObjects(){
        Object[] objects = new Object[parameters.size];
        for(int i = 0; i < objects.length; i++){
            objects[i] = parameters.get(i).toObjects();
        }
        return objects;
    }

    public boolean matches(Object[] objects){
        if(objects.length != amount()) return false;
        Bits bits = EventSignature.bits.get();
        int requiredLeft = parameters.count(Parameter::isRequired);
        bits.clear();

        for(int i = 0; i < objects.length; i++){
            if(!(objects[i] instanceof Object[])) return false;
            Object[] param = (Object[])objects[i];
            if(param.length != 3) return false;
            int found = 0;
            for(int j = 0; j < parameters.size; j++){
                Parameter parameter = parameters.get(j);
                if(parameter.equalsArr(param)){
                    if(bits.get(j) || found > 0) return false;
                    if(parameter.isRequired()){
                        requiredLeft--;
                    }
                    bits.set(j);
                    found++;
                }
            }
            if(found == 0) return false;
        }
        return requiredLeft <= 0;
    }

    public int amount(){
        return parameters.size;
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
    public static class Parameter{
        String name;
        boolean required;
        Class<?> type;

        public Object[] toObjects(){
            return new Object[]{name, type, required};
        }

        public boolean equalsArr(Object[] arr){
            return arr.length == 3 && Objects.equals(arr[0], name) && arr[1] == type && (arr[2] == Boolean.TRUE) == required;
        }

        public boolean isRequired(){
            return required;
        }
    }
}
