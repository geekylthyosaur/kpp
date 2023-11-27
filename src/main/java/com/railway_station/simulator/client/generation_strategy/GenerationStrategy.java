package com.railway_station.simulator.client.generation_strategy;

public interface GenerationStrategy {
    long minInterval = 500;
    long maxInterval = 2000;
    long getNextInterval();
}
