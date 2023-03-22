package com.example.mock;

import com.example.Cache;

import java.util.function.Function;

public class CacheMock<K, V> extends Cache<K, V> {
    private final InvokesCounter<K> getInvokesCounter = new InvokesCounter<>();
    private final InvokesCounter<K> putInvokesCounter = new InvokesCounter<>();
    private final Timer<K> cacheTimer = new Timer<>();

    public CacheMock(Function<K, V> producer) {
        super(producer);
    }

    public double getProbability(K key) {
        double total = getInvokesCounter.get(key);
        double part = putInvokesCounter.get(key);
        return total == 0 ? 0 : 1 - (part / total);
    }

    public long getLifetime(K key) {
        return cacheTimer.getTotalTime(key) / putInvokesCounter.get(key);
    }

    @Override
    protected void remove(K key) {
        cacheTimer.stop(key);
        super.remove(key);
    }

    @Override
    protected V put(K key) {
        putInvokesCounter.count(key);
        cacheTimer.start(key);
        return super.put(key);
    }

    @Override
    public V get(K key) {
        getInvokesCounter.count(key);
        return super.get(key);
    }
}
