package com.railway_station.simulator.client.generation_strategy;

import java.util.Random;

public class RandomTimeGeneration implements GenerationStrategy {
    @Override
    public long getNextInterval() {
        Random random = new Random();
        return minInterval + random.nextLong() % (maxInterval - minInterval + 1);
    }
}
