package arclibrary.utils.pools;

import arc.func.Prov;
import arc.util.pooling.Pool;

public class PoolImpl<T> extends Pool<T> {
    public final Prov<T> provider;

    public PoolImpl(Prov<T> provider) {
        this.provider = provider;
    }

    public PoolImpl(int initialCapacity, Prov<T> provider) {
        super(initialCapacity);
        this.provider = provider;
    }

    public PoolImpl(int initialCapacity, int max, Prov<T> provider) {
        super(initialCapacity, max);
        this.provider = provider;
    }

    @Override
    protected T newObject() {
        return provider.get();
    }
}
