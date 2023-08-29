package arclibrary.utils.pools;

import arc.struct.Seq;
import arc.util.pooling.Pool;

public abstract class ThreadSafePool<T>  extends Pool<T> {
    public ThreadSafePool() {
        super();
    }

    public ThreadSafePool(int initialCapacity) {
        super(initialCapacity);
    }

    public ThreadSafePool(int initialCapacity, int max) {
        super(initialCapacity, max);
    }

    @Override
    public synchronized T obtain() {
        return super.obtain();
    }

    @Override
    public void free(T object) {
        super.free(object);
    }

    @Override
    public void freeAll(Seq<T> objects) {
        super.freeAll(objects);
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }

    @Override
    public synchronized int getFree() {
        return super.getFree();
    }

}