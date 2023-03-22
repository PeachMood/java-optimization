package com.example;

import java.lang.ref.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Cache<K, V> {

    private class CacheReference extends WeakReference<V> {
        public final K key;

        public CacheReference(K key, V referent) {
            super(referent, Cache.this.referenceQueue);
            this.key = key;
        }
    }

    @SuppressWarnings("unchecked")
    private class ReferenceCleaner implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    CacheReference reference = (CacheReference) referenceQueue.remove();
                    remove(reference.key);
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    private final Function<K, V> producer;
    private final ReferenceQueue<V> referenceQueue = new ReferenceQueue<>();
    private final Map<K, CacheReference> cache = Collections.synchronizedMap(new HashMap<K, CacheReference>());
    private final Thread cleaner = new Thread(new ReferenceCleaner());

    public Cache(Function<K, V> producer) {
        this.producer = producer;
        this.cleaner.setDaemon(true);
        this.cleaner.start();
    }

    protected void remove(K key) {
        cache.remove(key);
    }

    protected V put(K key) {
        V value = producer.apply(key);
        cache.put(key, new CacheReference(key, value));
        return value;
    }

    public V get(K key) {
        CacheReference reference = cache.get(key);
        if (reference != null) {
            V value = reference.get();
            if (value != null) {
                return value;
            }
        }
        return put(key);
    }
}
