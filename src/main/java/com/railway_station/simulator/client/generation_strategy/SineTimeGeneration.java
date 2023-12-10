package com.railway_station.simulator.client.generation_strategy;

import com.railway_station.simulator.config.Configuration;

public class SineTimeGeneration implements GenerationStrategy {
    @Override
    public long getNextInterval() {
        double min = 0.0;
        double max = 1.0;
        double range = max - min;

        Configuration config = Configuration.getInstance();

        double t = Math.random() * range + min;
        return (long) (config.getMinClientServingTime() + ((config.getMaxClientServingTime() - config.getMinClientServingTime()) / 2) * (1 + Math.sin(2 * Math.PI * t)));
    }
}
