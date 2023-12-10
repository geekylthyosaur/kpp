package com.railway_station.simulator.config;

import com.railway_station.simulator.client.generation_strategy.FixedTimeGeneration;
import com.railway_station.simulator.client.generation_strategy.GenerationStrategy;
import com.railway_station.simulator.client.generation_strategy.RandomTimeGeneration;
import com.railway_station.simulator.client.generation_strategy.SineTimeGeneration;

public class Configuration {
    private static Configuration instance;
    private int cashRegistersCount;
    private int minClientServingTime;
    private int maxClientServingTime;
    private GenerationStrategy clientGenerationStrategy;
    private int maxClientsCountInsideBuilding;

    private Configuration() {
    }

    public int getCashRegistersCount() {
        return cashRegistersCount;
    }

    public void setCashRegistersCount(int cashRegistersCount) {
        this.cashRegistersCount = cashRegistersCount;
    }

    public int getMinClientServingTime() {
        return minClientServingTime;
    }

    public void setMinClientServingTime(int minClientServingTime) {
        this.minClientServingTime = minClientServingTime;
    }

    public int getMaxClientServingTime() {
        return maxClientServingTime;
    }

    public void setMaxClientServingTime(int maxClientServingTime) {
        this.maxClientServingTime = maxClientServingTime;
    }

    public int getMaxClientsCountInsideBuilding() {
        return maxClientsCountInsideBuilding;
    }

    public void setMaxClientsCountInsideBuilding(int maxClientsInside) {
        this.maxClientsCountInsideBuilding = maxClientsInside;
    }

    public void setClientGenerationStrategy(String generationStrategy) {
        switch (generationStrategy) {
            case "random_time_generation" -> clientGenerationStrategy = new RandomTimeGeneration();
            case "fixed_time_generation" -> {
                clientGenerationStrategy = new FixedTimeGeneration();
            }
            case "sin_time_generation" -> clientGenerationStrategy = new SineTimeGeneration();
            default -> clientGenerationStrategy = null;
        }
    }

    public GenerationStrategy getClientGenerationStrategy() {
        return clientGenerationStrategy;
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
}
