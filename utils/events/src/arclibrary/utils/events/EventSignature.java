package arclibrary.utils.events;

import arc.struct.*;
import arc.util.*;
import lombok.*;
import lombok.experimental.*;
import org.jetbrains.annotations.Nullable;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

public class EventSignature<T>{

    public static final String eventSignatureParam = "_\n_EVENT_SIGNATURE__";
    private static final ThreadLocal<Bits> bits = ThreadLocal.withInitial(Bits::new);
    private static final Class<Void> defaultClass = Void.class;
    ;
    final Seq<Parameter> parameters = new Seq<>();
    private final Class<T> myClass;

    public EventSignature(Class<T> myClass){
        this.myClass = myClass;

    }

    public static <T> EventSignature<T> fromClass(Class<T> clazz){
        if(clazz == null) throw new NullPointerException("class is null.");
        if(clazz.isAnonymousClass()) throw new IllegalArgumentException("class cannot be anonymous.");
        if(clazz.isInterface()) throw new IllegalArgumentException("class cannot be interface.");
        if(clazz.isEnum()) throw new IllegalArgumentException("enum is unsupported");
        EventSignature<T> signature = new EventSignature<>(clazz);
        Class<?> c = clazz;
        while(c != null){
            Field[] fields = c.getDeclaredFields();
            for(Field field : fields){
                if(!Modifier.isPublic(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) continue;
                boolean nullable = Structs.contains(field.getAnnotations(), it -> it.annotationType().getSimpleName().equals("Nullable"));
                if(nullable){
                    signature.optional(field.getName(), field.getType());
                }else{
                    signature.required(field.getName(), field.getType());
                }
            }
            c = c.getSuperclass();
        }
        return signature;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static EventSignature<Void> empty(){
        return new EventSignature<>(defaultClass);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static EventSignature<Void> make(String name, Class<?> type){
        return empty().with(name, type);
    }

    private static void throwMakeObjectNotSupport(){
        throw new IllegalArgumentException("makeObject() is not supported(has no candidate constructor)");
    }

    public boolean hasClass(){
        return myClass != defaultClass;
    }

    @SuppressWarnings("UnusedReturnValue")

    public EventSignature<T> with(String name, Class<?> type){
        return required(name, type);
    }

    @SuppressWarnings("UnusedReturnValue")
    public EventSignature<T> required(String name, Class<?> type){
        return with(name, type, true);
    }

    @SuppressWarnings("UnusedReturnValue")
    public EventSignature<T> optional(String name, Class<?> type){
        return with(name, type, false);
    }

    @SuppressWarnings("UnusedReturnValue")
    public EventSignature<T> with(String name, Class<?> type, boolean required){
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

    @SneakyThrows
    public T makeObject(EventReceiver<T> receiver){
        Constructor<?> constructor = findCandidateConstructor();
        if(constructor == null) throwMakeObjectNotSupport();
        T result;
        if(constructor.getParameterCount() == 0){
            //noinspection unchecked
            result = (T)constructor.newInstance();
            for(Parameter parameter : parameters){
                Reflect.set(result, parameter.name, receiver.getParameter(parameter.name));
            }
        }else{
            Object[] parameters = Arrays.stream(constructor.getAnnotation(ConstructorProperties.class)
                    .value())
                .map(receiver::getParameter)
                .toArray();
            //noinspection unchecked
            result = (T)constructor.newInstance(parameters);
        }
        return result;
    }

    public void checkSupportMakeObject(){
        if(findCandidateConstructor() == null) throwMakeObjectNotSupport();
    }

    /** If you want support for {@link lombok.AllArgsConstructor}
     * Do not forget to enable<br/><b>lombok.anyConstructor.addConstructorProperties</b><br/> in your
     * <a href="https://projectlombok.org/features/configuration">lombok.config</a>
     * */
    @Nullable
    private Constructor<?> findCandidateConstructor(){
        Constructor<?>[] declaredConstructors = myClass.getDeclaredConstructors();
        Constructor<?> candidate = null;
        for(Constructor<?> constructor : declaredConstructors){
            if(constructor.getParameterCount() == 0){
                candidate = constructor;
                break;
            }
        }
        if(candidate == null){
            candidate = Structs.find(declaredConstructors, it -> {
                if(it.getParameterCount() != parameters.size) return false;
                /*Support for @lombok.AllArgsConstructor do not forget to enable `` in lombok.config*/
                ConstructorProperties annotation = it.getAnnotation(ConstructorProperties.class);
                if(annotation == null) return false;
                String[] value = annotation.value();
                for(String s : value){
                    if(!parameters.contains(parameter -> parameter.name.equals(s))) return false;
                }
                return true;
            });
        }
        return candidate;
    }

    public Class<T> targetClass(){
        return myClass;
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
