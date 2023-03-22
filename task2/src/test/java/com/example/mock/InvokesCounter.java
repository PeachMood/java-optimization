package com.example.mock;

import java.util.HashMap;
import java.util.Map;

public class InvokesCounter<T> {
    Map<T, Integer> invokes = new HashMap<>();

    public void count(T arg) {
        invokes.merge(arg, 1, Integer::sum);
    }

    public int get(T arg) {
        return invokes.getOrDefault(arg, 0);
    }
}
