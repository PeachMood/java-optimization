package com.example;

import com.example.chart.LineChart;
import com.example.mock.CacheMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

class CacheTest {
    // About 68% of the values from the normal distribution are at a distance of no more than one standard deviation σ from the mean
    // About 95% of the values lie at a distance of no more than two standard deviations
    // And 99.7% no more than three
    private final int MEAN_VALUE = 1000;
    private final int STD_VALUE = 300;
    private final int CACHE_SIZE = 2 * MEAN_VALUE;
    private final int REQUESTS_NUMBER = 100000000;

    private final Path chartsPath =  Paths.get("charts");
    private final Random random = new Random();
    private final Function<Integer, String> producer = String::valueOf;
    private CacheMock<Integer, String> cacheMock;

    private Integer getRandomKey() {
        int randomKey = (int) (random.nextGaussian() * STD_VALUE + MEAN_VALUE);
        if (randomKey < 0 || randomKey >= CACHE_SIZE) {
            return getRandomKey();
        }
        return randomKey;
    }

    @BeforeEach
    public void init() {
        cacheMock = new CacheMock<>(producer);
    }

    @Test
    @DisplayName("Testing get method returns values from cache for random keys")
    public void get_ReturnsValuesFromCache_ForRandomKeys() throws IOException {
        for (int i = 0; i < REQUESTS_NUMBER; ++i) {
            cacheMock.get(getRandomKey());
        }

        LineChart chart = new LineChart("WeakReferenceCache hits distribution", "Values", "Returns from cache");
        for (int value = 0; value < CACHE_SIZE; ++value) {
            chart.addData(value, cacheMock.getProbability(value));
        }

        chart.saveChart(Paths.get(chartsPath.toString(), "WeakReferenceCache.jpg"));
    }

    @Test
    @DisplayName("Testing get method clears cache for random keys")
    public void get_ClearsCache_ForRandomKeys() throws IOException {
        for (int i = 0; i < REQUESTS_NUMBER; ++i) {
            cacheMock.get(getRandomKey());
        }

        LineChart chart = new LineChart("WeakReferenceCache cache lifetime", "Values", "Lifetime");
        for (int value = 0; value < CACHE_SIZE; ++value) {
            chart.addData(value, cacheMock.getLifetime(value));
        }

        chart.saveChart(Paths.get(chartsPath.toString(), "WeakReferenceCache_CacheLifetime.jpg"));
    }
}