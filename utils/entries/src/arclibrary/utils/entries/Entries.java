package arclibrary.utils.entries;


import arc.struct.*;

public class Entries {
    //region creation
    public static IntFloatMap.Entry intFloatEntry(int key, float value) {
        IntFloatMap.Entry entry = new IntFloatMap.Entry();
        entry.key = key;
        entry.value = value;
        return entry;
    }

    public static IntIntMap.Entry intIntEntry(int key, int value) {
        IntIntMap.Entry entry = new IntIntMap.Entry();
        entry.key = key;
        entry.value = value;
        return entry;
    }

    public static <V> IntMap.Entry<V> intEntry(int key, V value) {
        IntMap.Entry<V> entry = new IntMap.Entry<>();
        entry.key = key;
        entry.value = value;
        return entry;
    }

    public static <V> LongMap.Entry<V> longEntry(long key, V value) {
        LongMap.Entry<V> entry = new LongMap.Entry<>();
        entry.key = key;
        entry.value = value;
        return entry;
    }

    public static <K> ObjectFloatMap.Entry<K> objectFloatEntry(K key, float value) {
        ObjectFloatMap.Entry<K> entry = new ObjectFloatMap.Entry<>();
        entry.key = key;
        entry.value = value;
        return entry;
    }

    public static <K> ObjectIntMap.Entry<K> objectIntEntry(K key, int value) {
        ObjectIntMap.Entry<K> entry = new ObjectIntMap.Entry<>();
        entry.key = key;
        entry.value = value;
        return entry;
    }

    public static <K, V> ObjectMap.Entry<K, V> objectEntry(K key, V value) {
        ObjectMap.Entry<K, V> entry = new ObjectMap.Entry<>();
        entry.key = key;
        entry.value = value;
        return entry;
    }

    //endregion
    //region copy
    public static IntFloatMap.Entry copy(IntFloatMap.Entry entry) {
        return intFloatEntry(entry.key, entry.value);
    }

    public static IntIntMap.Entry copy(IntIntMap.Entry entry) {
        return intIntEntry(entry.key, entry.value);
    }

    public static <V> IntMap.Entry<V> copy(IntMap.Entry<V> entry) {
        return intEntry(entry.key, entry.value);
    }

    public static <V> LongMap.Entry<V> copy(LongMap.Entry<V> entry) {
        return longEntry(entry.key, entry.value);
    }

    public static <K> ObjectFloatMap.Entry<K> copy(ObjectFloatMap.Entry<K> entry) {
        return objectFloatEntry(entry.key, entry.value);
    }

    public static <K> ObjectIntMap.Entry<K> copy(ObjectIntMap.Entry<K> entry) {
        return objectIntEntry(entry.key, entry.value);
    }

    public static <K, V> ObjectMap.Entry<K, V> copy(ObjectMap.Entry<K, V> entry) {
        return objectEntry(entry.key, entry.value);
    }
//endregion
}
