package com.railway_station.simulator.client.generation_strategy;

import com.railway_station.simulator.config.Configuration;

public class FixedTimeGeneration implements GenerationStrategy {
    @Override
    public long getNextInterval() {
        Configuration config = Configuration.getInstance();
        long minServingTime = config.getMinClientServingTime();
        long maxServingTime = config.getMaxClientServingTime();
        return (minServingTime + maxServingTime) / 2;
    }
}
