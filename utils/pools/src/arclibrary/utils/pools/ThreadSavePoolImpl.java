package arclibrary.utils.pools;

import arc.func.Prov;
import arc.util.pooling.Pool;

public class ThreadSavePoolImpl<T> extends ThreadSavePool<T> {
    public final Prov<T> provider;

    public ThreadSavePoolImpl(Prov<T> provider) {
        this.provider = provider;
    }

    public ThreadSavePoolImpl(int initialCapacity, Prov<T> provider) {
        super(initialCapacity);
        this.provider = provider;
    }

    public ThreadSavePoolImpl(int initialCapacity, int max, Prov<T> provider) {
        super(initialCapacity, max);
        this.provider = provider;
    }

    @Override
    protected T newObject() {
        return provider.get();
    }
}
