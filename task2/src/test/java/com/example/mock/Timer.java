package com.example.mock;

import java.util.HashMap;
import java.util.Map;

public class Timer<T> {
    private final Map<T, Long> totalTime = new HashMap<>();
    private final Map<T, Long> currentTime = new HashMap<>();

    public void start(T arg) {
        currentTime.put(arg, System.currentTimeMillis());
    }

    public void stop(T arg) {
        totalTime.merge(arg, System.currentTimeMillis() - currentTime.get(arg), Long::sum);
    }

    public long getTotalTime(T arg) {
        stop(arg);
        return totalTime.getOrDefault(arg, 0L);
    }
}
