package com.railway_station.simulator.config;

import com.railway_station.simulator.client.generation_strategy.FixedTimeGeneration;
import com.railway_station.simulator.client.generation_strategy.GenerationStrategy;
import com.railway_station.simulator.client.generation_strategy.RandomTimeGeneration;
import com.railway_station.simulator.client.generation_strategy.SineTimeGeneration;

public class Configuration {
    private static Configuration instance;
    private int cashRegisterCount;
    private int entryCount;
    private int serviceTimeMin;
    private int serviceTimeMax;
    private GenerationStrategy clientGenerationStrategy;
    private int maxClientsInside;
    private Thread clientGenerationThread;

    private Configuration() {
    }

    public int getCashRegisterCount() {
        return cashRegisterCount;
    }

    public void setCashRegisterCount(int cashRegisterCount) {
        this.cashRegisterCount = cashRegisterCount;
    }

    public int getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(int entryCount) {
        this.entryCount = entryCount;
    }

    public int getServiceTimeMin() {
        return serviceTimeMin;
    }

    public void setServiceTimeMin(int serviceTimeMin) {
        this.serviceTimeMin = serviceTimeMin;
    }

    public int getServiceTimeMax() {
        return serviceTimeMax;
    }

    public void setServiceTimeMax(int serviceTimeMax) {
        this.serviceTimeMax = serviceTimeMax;
    }

    public int getMaxClientsInside() {
        return maxClientsInside;
    }

    public void setMaxClientsInside(int maxClientsInside) {
        this.maxClientsInside = maxClientsInside;
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

    public void setClientGenerationThread(Thread thread) {
        clientGenerationThread = thread;
    }

    public Thread getClientGenerationThread() {
        return clientGenerationThread;
    }
}
