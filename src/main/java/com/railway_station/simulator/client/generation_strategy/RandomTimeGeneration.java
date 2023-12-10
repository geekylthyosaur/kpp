package com.railway_station.simulator.client.generation_strategy;

import com.railway_station.simulator.config.Configuration;

import java.util.Random;

public class RandomTimeGeneration implements GenerationStrategy {
    @Override
    public long getNextInterval() {
        Random random = new Random();
        Configuration config = Configuration.getInstance();
        return config.getMinClientServingTime() + random.nextLong() % (config.getMaxClientServingTime() - config.getMinClientServingTime() + 1);
    }
}
