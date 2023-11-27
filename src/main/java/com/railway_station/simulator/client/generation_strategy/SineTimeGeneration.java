package com.railway_station.simulator.client.generation_strategy;

public class SineTimeGeneration implements GenerationStrategy {
    @Override
    public long getNextInterval() {
        double min = 0.0;
        double max = 1.0;
        double range = max - min;

        double t = Math.random() * range + min;
        return (long) (minInterval + ((maxInterval - minInterval) / 2) * (1 + Math.sin(2 * Math.PI * t)));
    }
}
